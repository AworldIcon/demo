package activity.kotlin.coder.com.activity.notify;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.jpush.im.android.api.JMessageClient;
import activity.kotlin.coder.com.R;
/**
 * Created by ${chenyn} on 16/4/7.
 *
 * @desc :设置通知类型
 */
public class NotifyTypeActivity extends Activity implements View.OnClickListener {
    public static final int NOTI_MODE_NO_NOTIFICATION = 0;
    public static final int NOTI_MODE_DEFAULT = 1;
    public static final int NOTI_MODE_NO_SOUND = 2;
    public static final int NOTI_MODE_NO_VIBRATE = 3;
    public static final int NOTI_MODE_SILENCE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_notify_type);
        Button bt_one = (Button) findViewById(R.id.bt_one);
        Button bt_two = (Button) findViewById(R.id.bt_two);
        Button bt_three = (Button) findViewById(R.id.bt_three);
        Button bt_four = (Button) findViewById(R.id.bt_four);
        Button bt_five = (Button) findViewById(R.id.bt_five);

        bt_one.setOnClickListener(this);
        bt_two.setOnClickListener(this);
        bt_three.setOnClickListener(this);
        bt_four.setOnClickListener(this);
        bt_five.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_one:
                JMessageClient.setNotificationMode(NOTI_MODE_NO_NOTIFICATION);
                showToast();
                break;
            case R.id.bt_two:
                JMessageClient.setNotificationMode(NOTI_MODE_DEFAULT);
                showToast();
                break;
            case R.id.bt_three:
                JMessageClient.setNotificationMode(NOTI_MODE_NO_SOUND);
                showToast();
                break;
            case R.id.bt_four:
                JMessageClient.setNotificationMode(NOTI_MODE_NO_VIBRATE);
                showToast();
                break;
            case R.id.bt_five:
                JMessageClient.setNotificationMode(NOTI_MODE_SILENCE);
                showToast();
                break;
            default:
                break;
        }
    }

    public void showToast() {
        Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT).show();
    }
}
