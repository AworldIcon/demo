package activity.kotlin.coder.com.activity.createmessage;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;
import activity.kotlin.coder.com.R;
/**
 * Created by ${chenyn} on 16/3/29.
 *
 * @desc :创建单聊文本消息,创建消息有两个方式,快捷方式是在标准步骤上封装了一层是sdk提供的更加快捷的实现创建
 * 消息的方式,快捷方式和标准步骤方式总体区别在于快捷方式不能自定义fromName(几种消息类型同理),标准步骤创建
 * 消息则可以,同时标准步骤创建需要创建会话和content这样就可以拿到其中的某些属性,还可以通过content设置附加
 * 消息体,这是快捷方式做不到的.接收方收到通知,点击通知会对收到的分类属性进行打印.{@link im.sdk.debug.activity.TypeActivity}
 */
public class CreateSigTextMessageActivity extends Activity {

    private static final String TAG = "CreateSigTextMessage";
    private EditText mEt_name;
    private EditText mEt_text;
    private Button mBt_send;
    public static final String TEXT_MESSAGE = "text_message";
    private EditText mEt_appkey;
    private Button mBt_send_con;
    private Conversation mConversation;
    private EditText mEt_customName;
    private TextView mTv_showContent;
    private EditText mEt_extraKey;
    private EditText mEt_extraValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    //创建单聊消息
    private void initData() {
        mBt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEt_name.getText().toString();
                String text = mEt_text.getText().toString();
                String appkey = mEt_appkey.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    Message message = JMessageClient.createSingleTextMessage(name, appkey, text);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //发送动作建议放在callback之后
                    JMessageClient.sendMessage(message);
                } else {
                    Toast.makeText(getApplicationContext(), "输入发送对象userName", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //创建单聊消息,本方式可以通过创建跨应用单聊会话实现跨应用(如果appKey是空的话就会默认本应用)
        mBt_send_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEt_name.getText().toString();
                String text = mEt_text.getText().toString();
                String appkey = mEt_appkey.getText().toString();

                String customName = mEt_customName.getText().toString();
                String extraKey = mEt_extraKey.getText().toString();
                String extraValue = mEt_extraValue.getText().toString();

                MessageContent content = new TextContent(text);

                content.setStringExtra(extraKey, extraValue);

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "请输入userName", Toast.LENGTH_SHORT).show();
                    return;
                }
                mConversation = JMessageClient.getSingleConversation(name, appkey);
                if (mConversation == null) {
                    mConversation = Conversation.createSingleConversation(name, appkey);
                }
                Message message = mConversation.createSendMessage(content, customName);
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Log.i(TAG, "Conversation.createSingleConversation" + ", responseCode = " + i + " ; LoginDesc = " + s);
                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "Conversation.createSingleConversation" + ", responseCode = " + i + " ; LoginDesc = " + s);
                            Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                JMessageClient.sendMessage(message);
                ContentType contentType = content.getContentType();
                Map<String, String> stringExtras = content.getStringExtras();
                mTv_showContent.append("contentType = " + contentType + "\nstringExtras = " + stringExtras);
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_create_single_text_message);

        mEt_name = (EditText) findViewById(R.id.et_name);
        mEt_text = (EditText) findViewById(R.id.et_text);
        mBt_send = (Button) findViewById(R.id.bt_send);
        mEt_appkey = (EditText) findViewById(R.id.et_appkey);
        mBt_send_con = (Button) findViewById(R.id.bt_send_con);
        mEt_customName = (EditText) findViewById(R.id.et_custom_name);
        mTv_showContent = (TextView) findViewById(R.id.tv_show_content);
        mEt_extraKey = (EditText) findViewById(R.id.et_extra_key);
        mEt_extraValue = (EditText) findViewById(R.id.et_extra_value);
    }
}
