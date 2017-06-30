package activity.kotlin.coder.com.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import activity.kotlin.coder.com.R;
import activity.kotlin.coder.com.activity.conversation.IsShowNotifySigActivity;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class SingleConversationActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private EditText reply_write_edit;
    private Button reply_post_button;
    private ListView talk_list;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mData;
    private Conversation singleConversation;
    private RelativeLayout reply_post_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JMessageClient.registerEventReceiver(this);
        setContentView(R.layout.activity_single_conversation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("回话进行中");
        reply_write_edit= (EditText) findViewById(R.id.reply_write_edit);
        reply_post_button= (Button) findViewById(R.id.reply_post_button);
        talk_list= (ListView) findViewById(R.id.talk_list);
        reply_post_layout= (RelativeLayout) findViewById(R.id.reply_post_layout);
        mData = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mData);
        talk_list.setAdapter(mAdapter);
        loginHideSoftInputWindow();
        enterConversation();
        events();
    }

    private void events() {
        reply_post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.add(reply_write_edit.getText().toString());
                mAdapter.notifyDataSetChanged();
                talk_list.smoothScrollToPosition(mData.size()-1);
               // talk_list.scrollBy(0,100);
                loginHideSoftInputWindow();
                Message message = JMessageClient.createSingleTextMessage("zw123456", reply_write_edit.getText().toString());
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Log.d("zw--send", "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            talk_list.setSelection(mData.size()-1);
                            reply_write_edit.setText("");
                        } else {
                            Log.d("zw--send", "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                            Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //发送动作建议放在callback之后
                JMessageClient.sendMessage(message);
            }
        });


    }

    private void enterConversation() {
        JMessageClient.getUserInfo("zw123456", "", new GetUserInfoCallback() {

            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                if (responseCode == 0) {
                    //调用enterSingleConversation之后，收到对应用户发来的消息通知栏将不会有通知提示
                    /**在调用这个接口时sdk会对未读消息数进行重置处理,但是如果过程中再次收到信息还是会累加未读消息数,用户可以通过conversation.resetUnreadCount();
                     在需要的时候进行重置处理*/
//                    JMessageClient.enterSingleConversation("zw12345", "");
//                    Toast.makeText(getApplicationContext(), info.getUserID() + "进入会话成功" + info.getUserName(), Toast.LENGTH_LONG).show();
                    singleConversation = JMessageClient.getSingleConversation("zw123456");
                    List<Message> allMessage = singleConversation.getAllMessage();
               //     singleConversation.get
                    //可以拿到之前所有的聊天信息,上下这两步有冲突
                    for (int i = 0; i < allMessage.size(); i++) {
                        mData.add(allMessage.get(i).getFromUser().getUserName()+"---->说： "+((TextContent) allMessage.get(i).getContent()).getText().trim());
                    }
                } else {
                    reply_post_layout.setVisibility(View.GONE);
                    Toast.makeText(SingleConversationActivity.this, "没有此会话", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onEvent(MessageEvent event) {
        Log.d("ZW--MESG", "onEvent: ");
        Message msg = event.getMessage();
        final MessageContent content = msg.getContent();
        switch (msg.getContentType()) {
            case text:
                TextContent textContent = (TextContent) content;
                final String str = textContent.getText();
                mData.add(msg.getFromUser().getUserName()+"---->说： "+str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // talk_list.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        talk_list.smoothScrollToPosition(mData.size()-1);
                      //  talk_list.scrollBy(0,100);
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }
    //隐藏键盘
    public void loginHideSoftInputWindow()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
        {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
