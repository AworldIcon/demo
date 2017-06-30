package activity.kotlin.coder.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class ConversationActivity extends AppCompatActivity {
    TextView content;
    String username;
    Button send;
    EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
//        content=(TextView) findViewById(R.id.content);
//        send=(Button) findViewById(R.id.send);
//        edit=(EditText) findViewById(R.id.edit);
        username="zw1234567";
        JMessageClient.registerEventReceiver(this);
        Conversation.createSingleConversation(username);
        JMessageClient.getUserInfo(username, new GetUserInfoCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage, UserInfo info) {
                if (responseCode == 0) {
                    //调用enterSingleConversation之后，收到对应用户发来的消息通知栏将不会有通知提示
                    /**在调用这个接口时sdk会对未读消息数进行重置处理,但是如果过程中再次收到信息还是会累加未读消息数,用户可以通过conversation.resetUnreadCount();
                     在需要的时候进行重置处理*/
                    JMessageClient.enterSingleConversation(username);
                    //Toast.makeText(getApplicationContext(), "进入会话成功", Toast.LENGTH_SHORT).show();
                    content.setText(responseMessage);
                } else {
                    Log.d("zw--conver",responseCode+"--code--"+responseMessage);
                   // Toast.makeText(IsShowNotifySigActivity.this, "没有此会话", Toast.LENGTH_SHORT).show();
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = JMessageClient.createSingleTextMessage(username, edit.getText().toString());
                message.setOnSendCompleteCallback(new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            Log.d("zw--send", "JMessageClient.createSingleTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                            Conversation singleConversation = JMessageClient.getSingleConversation(username);
                            Message latestMessage = singleConversation.getLatestMessage();
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

       // latestMessage.
    }
    /**
     * #################    处理消息事件    #################
     */
    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        final MessageContent content1 = msg.getContent();
        switch (msg.getContentType()) {
            case text:
                TextContent textContent = (TextContent) content1;
                final String str = textContent.getText();
                content.setText(str);
                break;
        }
    }

}
