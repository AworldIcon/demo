package activity.kotlin.coder.com.activity.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import activity.kotlin.coder.com.R;
/**
 * Created by ${chenyn} on 16/3/30.
 *
 * @desc :会话相关主界面
 */
public class ConversationActivity extends Activity implements View.OnClickListener {

    private TextView mTv_showConvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        setContentView(R.layout.activity_conversation);
        Button bt_getInfo = (Button) findViewById(R.id.bt_get_conversation_info);
        Button bt_setInfo = (Button) findViewById(R.id.bt_get_info);
        Button bt_enterConversation = (Button) findViewById(R.id.bt_enter_conversation);
        Button bt_deleteConversation = (Button) findViewById(R.id.bt_delete_conversation);
        Button bt_localGetConvList = (Button) findViewById(R.id.bt_localGetConvList);
        Button bt_getConvList = (Button) findViewById(R.id.bt_getConvList);

        mTv_showConvList = (TextView) findViewById(R.id.tv_showConvList);

        bt_getInfo.setOnClickListener(this);
        bt_setInfo.setOnClickListener(this);
        bt_enterConversation.setOnClickListener(this);
        bt_deleteConversation.setOnClickListener(this);
        bt_localGetConvList.setOnClickListener(this);
        bt_getConvList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_conversation_info://获取会话的各种属性
                Intent intentSet = new Intent(getApplicationContext(), GetConversationInfoActivity.class);
                startActivity(intentSet);
                break;
            case R.id.bt_get_info://排序message
                Intent intentGet = new Intent(getApplicationContext(), OrderMessageActivity.class);
                startActivity(intentGet);
                break;
            case R.id.bt_enter_conversation://进入会话不展示通知
                Intent intentEnterSingle = new Intent(getApplicationContext(), IsShowNotifySigActivity.class);
                startActivity(intentEnterSingle);
                break;
            case R.id.bt_delete_conversation://删除会话
                Intent intentDelete = new Intent(getApplicationContext(), DeleteConversationActivity.class);
                startActivity(intentDelete);
                break;
            case R.id.bt_localGetConvList://本地获取会话列表默认降序
                mTv_showConvList.setText("降序 : \n");
                List<Conversation> conversationList = JMessageClient.getConversationList();
                if (conversationList != null) {
                    for (Conversation convList : conversationList) {
                        if (convList.getType().toString().equals("single")) {
                            UserInfo userInfo = (UserInfo) convList.getTargetInfo();
                            mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  用户名 : " + userInfo.getUserName() + "\n");
                        }
                        if (convList.getType().toString().equals("group")) {
                            GroupInfo groupInfo = (GroupInfo) convList.getTargetInfo();
                            mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  群组id : " + groupInfo.getGroupID() + "\n");
                        }
                    }
                } else {
                    Toast.makeText(ConversationActivity.this, "该用户没有会话", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_getConvList://本地获取会话列表不排序
                mTv_showConvList.setText("不排序 : \n");
                List<Conversation> getConvList = JMessageClient.getConversationListByDefault();
                if (getConvList != null) {
                    for (Conversation convList : getConvList) {
                        if (convList.getType().toString().equals("single")) {
                            UserInfo userInfo = (UserInfo) convList.getTargetInfo();
                            mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  用户名 : " + userInfo.getUserName() + "\n");
                        }
                        if (convList.getType().toString().equals("group")) {
                            GroupInfo groupInfo = (GroupInfo) convList.getTargetInfo();
                            mTv_showConvList.append("会话类型 : " + convList.getType() + " - - -  群组id : " + groupInfo.getGroupID() + "\n");
                        }
                    }
                } else {
                    Toast.makeText(ConversationActivity.this, "该用户没有会话", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}

