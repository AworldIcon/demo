package activity.kotlin.coder.com;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import activity.kotlin.coder.com.selfview.BarChartView;
import activity.kotlin.coder.com.selfview.PermissionListener;
import activity.kotlin.coder.com.selfview.PermissionManager;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class Main2Activity extends AppCompatActivity {

    private BarChartView chartView;
    private SeekBar seekBar;
    private List<Float> floats;
    private RadioGroup rg,rg1,rg2;
    public static int REQUEST_CODE_CAMERA=101;
    private         PermissionManager helper;
    private Button login_jm,resert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d("zw--newIntent---->","create");
        chartView = (BarChartView) findViewById(R.id.barchart);
        seekBar = (SeekBar) findViewById(R.id.value_seek);
        rg = (RadioGroup) findViewById(R.id.rg);
        rg1 = (RadioGroup) findViewById(R.id.rg1);
        rg2 = (RadioGroup) findViewById(R.id.rg2);
        login_jm=(Button) findViewById(R.id.login);
        resert=(Button) findViewById(R.id.resert);
        float[] progress = new float[]{100,120,130,140,150,160,170};
        chartView.setBarChartList(progress);
        chartView.setMax(190);
        floats = new ArrayList<>();

//        seekBar.setProgress(15);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (progress > 15) {
//                    floats.add((float) (120+progress*5));
//                }else{
//                    if (floats.size() > 0) {
//                        floats.remove(0);
//                    }
//                }
//                list2Float(floats);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d("value-y2",floats.get(floats.size()-1)+"+++");
//            }
//        });

        login_jm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JMessageClient.login("zw1234567", "123456", new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.d("zw--login--",s+"--code--"+i);
                        startActivity(new Intent(Main2Activity.this,ConversationActivity.class));
                    }
                });

            }
        });
        resert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JMessageClient.register("zw1234567", "123456", new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.d("zw--register--",s+"--code--"+i);
                    }
                });
            }
        });
        events();
       // getpermission();

    }

    private void events() {
        chartView.setOnViewClick(new BarChartView.onViewClick() {
            @Override
            public void onClick(int index, int value) {
                Toast.makeText(Main2Activity.this, "onClick: "+index+"  "+value, Toast.LENGTH_SHORT).show();
            }
        });


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.hide_gird) {
                    chartView.setHideGirdLine(false);
                }else if(checkedId == R.id.show_gird){
                    chartView.setHideGirdLine(true);
                }
            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.line) {
                    chartView.setCharType(1);
                }else if(checkedId == R.id.bar){
                    chartView.setCharType(0);
                }
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.enable) {
                    chartView.setBarPressEnable(true);
                }else if(checkedId == R.id.unable){
                    chartView.setBarPressEnable(false);
                }
            }
        });
    }

    private void getpermission() {
        helper = PermissionManager.with(Main2Activity.this)
                //添加权限请求码
                .addRequestCode(Main2Activity.REQUEST_CODE_CAMERA)
                //设置权限，可以添加多个权限
                .permissions(Manifest.permission.CALL_PHONE)
                .permissions(Manifest.permission.CAMERA)//多组权限同时申请的最后添加的在成功回调的0位置
                //设置权限监听器
                .setPermissionsListener(new PermissionListener() {

                    @Override
                    public void onGranted(String[] permissions) {

                        if(permissions[0].equals(Manifest.permission.CAMERA)){
                            //当权限被授予时调用
                            // dialog();
                            Toast.makeText(Main2Activity.this, permissions[0]+" agree",Toast.LENGTH_LONG).show();
                            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/yunketang/HBSI_headbg/");
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File file = new File(dir, "yunketang_head.png");
                            Uri imageUri = Uri.fromFile(file);
                            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(openCameraIntent,0);
                        }

                    }

                    @Override
                    public void onDenied(String[] permissions){
                        Log.d("zw--",permissions.length+"***");
                        //用户拒绝该权限时调用
                        // Toast.makeText(Main2Activity.this, "Camera Permission refused",Toast.LENGTH_LONG).show();
                        for(int i=0;i<permissions.length;i++){
                            Log.d("zw--",permissions[i]);
                            Toast.makeText(Main2Activity.this, permissions[i]+" refused",Toast.LENGTH_LONG).show();
                        }
                        dialog();
                    }

                    @Override
                    public void onShowRationale(String[] permissions) {
                        Toast.makeText(Main2Activity.this, "Camera Permission sys refused",Toast.LENGTH_LONG).show();
                        //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                        dialog();
                    }
                })
                //请求权限
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                helper.onPermissionResult(permissions, grantResults);
                break;
        }
    }
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("当前应用缺少必要条件，是否跳转设置打开相应权限？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //跳转手机权限

                //未测
//                Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.putExtra("com.cc.pc.designproject", BuildConfig.APPLICATION_ID);
//                try {
//                    activity.startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                //可以
//                Uri packageURI = Uri.parse("package:" + activity.getPackageName());
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                activity.startActivity(intent);

                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //判断android版本
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    localIntent.setData(Uri.fromParts("package",getPackageName(), null));
                }
//                else if (Build.VERSION.SDK_INT <= 8) {
//                    localIntent.setAction(Intent.ACTION_VIEW);
//                    localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
//                    localIntent.putExtra("com.android.settings.ApplicationPkgName",activity.getPackageName());
//                }
                startActivity(localIntent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void list2Float(List<Float> floats){
        float[] progress = new float[floats.size()];
        for (int i = 0; i < floats.size(); i++) {
            progress[i] = floats.get(i);
        }
        chartView.setBarChartList(progress);
    }
    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        Log.d("zw--newIntent---->","resume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        Log.d("zw--newIntent---->","pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("zw--newIntent---->","destroy");
    }

    @Override
    protected void onStart() {
        Log.d("zw--newIntent---->","start");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d("zw--newIntent---->","onstop");
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("zw--newIntent---->",getIntent().getExtras().getString(JPushInterface.EXTRA_ALERT)+"***");
    }
}