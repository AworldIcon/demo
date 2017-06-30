package activity.kotlin.coder.com.activity.createmessage;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoListCallback;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import activity.kotlin.coder.com.R;

/**
 * Created by ${chenyn} on 16/3/31.
 *
 * @desc :创建群聊文本信息
 */
public class CreateGroupTextMsgActivity extends Activity {

    private EditText mEt_id;
    private EditText mEt_text;
    private Button   mBt_send;
    public static final String GROUP_NOTIFICATION      = "group_notification";
    public static final String GROUP_NOTIFICATION_LIST = "group_notification_list";
    private EditText     mEt_atUserName;
    private Conversation mConversation;
    private TextView     mTv_showAtList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    //创建群聊文本消息.可@群成员
    private void initData() {
        mBt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mEt_id.getText().toString();
                String text = mEt_text.getText().toString();
                String atName = mEt_atUserName.getText().toString();
                if (!TextUtils.isEmpty(id) && TextUtils.isEmpty(atName)) {
                    long gid = Long.parseLong(id);
                    Message message = JMessageClient.createGroupTextMessage(gid, text);
                    message.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0) {
                                mTv_showAtList.setText("");
                                Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                mTv_showAtList.setText("");
                                mTv_showAtList.append("发送失败 responseCode = " + i + "; responseMessage = " + s);
                                Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    JMessageClient.sendMessage(message);
                } else if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(atName)) {
                    final List<UserInfo> infosList = new ArrayList<>();
                    final MessageContent content = new TextContent(text);
                    mConversation = JMessageClient.getGroupConversation(Long.parseLong(id));
                    if (null == mConversation) {
                        mConversation = Conversation.createGroupConversation(Long.parseLong(id));
                    }
                    JMessageClient.getUserInfo(atName, new GetUserInfoCallback() {
                        @Override
                        public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                            if (responseCode == 0) {
                                infosList.add(info);
                                Message message = mConversation.createSendMessage(content, infosList, null);//此处null可以替换为自定义fromName
                                message.getAtUserList(new GetUserInfoListCallback() {
                                    @Override
                                    public void gotResult(int responseCode, String responseMessage, List<UserInfo> userInfoList) {
                                        if (responseCode == 0) {
                                            StringBuilder sb = new StringBuilder();
                                            mTv_showAtList.setText("@的成员列表 :\n");
                                            for (UserInfo info : userInfoList) {
                                                String userName = info.getUserName();
                                                sb.append(userName);
                                                sb.append("\n");
                                            }
                                            mTv_showAtList.append(sb.toString());
                                        } else {
                                            mTv_showAtList.setText("");
                                            mTv_showAtList.append("获取 @ 列表失败 responseCode = " + responseCode + "; responseMessage = " + responseMessage);
                                        }
                                    }
                                });

                                message.setOnSendCompleteCallback(new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        if (i == 0) {
                                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                JMessageClient.sendMessage(message);
                            } else {
                                mTv_showAtList.setText("");
                                Toast.makeText(CreateGroupTextMsgActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                mTv_showAtList.append("操作失败 responseCode = " + responseCode + "; responseMessage = " + responseMessage);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "请正确输入相关参数", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_create_group_text_message);

        mEt_id = (EditText) findViewById(R.id.et_id);
        mEt_text = (EditText) findViewById(R.id.et_text);
        mBt_send = (Button) findViewById(R.id.bt_send);
        mEt_atUserName = (EditText) findViewById(R.id.et_atUserName);
        mTv_showAtList = (TextView) findViewById(R.id.tv_showAtList);
    }
}
