package activity.kotlin.coder.com.activity.createmessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import activity.kotlin.coder.com.R;
import activity.kotlin.coder.com.activity.TypeActivity;

public class ShowDownloadVoiceInfoActivity extends Activity {
    private TextView mTv_showDownloadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(TypeActivity.DOWNLOAD_INFO);
        mTv_showDownloadInfo.append("down load info :" + "\n" + stringExtra);
    }

    private void initView() {
        setContentView(R.layout.activity_show_download_voice_info);

        mTv_showDownloadInfo = (TextView) findViewById(R.id.tv_show_download_info);
    }
}
