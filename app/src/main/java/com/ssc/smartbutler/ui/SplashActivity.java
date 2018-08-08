package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/8 21:20
 *  描述：     闪屏页
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;


/*
 * 1.延时2000ms
 * 2.判断程序是否是第一次运行
 * 3.自定义字体
 * 4.Activity全屏主题
 */

import com.ssc.smartbutler.MainActivity;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;
import com.ssc.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

import static com.ssc.smartbutler.utils.StaticClass.HANDLER_EXIT;
import static com.ssc.smartbutler.utils.StaticClass.HANDLER_SPLASH;
import static com.ssc.smartbutler.utils.StaticClass.SHARE_IS_FIRST;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private TextView tv_splash;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    L.i(TAG, isFirst() + "");
                    if (isFirst()) {
                        ShareUtil.putBoolean(SplashActivity.this, SHARE_IS_FIRST, false);
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    finish();
                    break;
                case HANDLER_EXIT:
                    finish();
                default:
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

        getPermission();

    }


    private void initView() {
        tv_splash = (TextView) findViewById(R.id.tv_splash);


        //设置字体
        UtilTools.setFont(this, tv_splash);


    }

    private void getPermission() {
        String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECEIVE_SMS};
        List<String> mPermissionList = new ArrayList<>();

        /**
         * 判断哪些权限未授予
         * 把未授权的权限放到mPermissionList中
         */
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(SplashActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        /**
         * 判断mPermissionList是否为空
         */
        if (mPermissionList.isEmpty()) {
            //未授予的权限为空，表示都授予了
            //delayEntryPage();
            //L.i(TAG,mPermissionList.get(0));
            handler.sendEmptyMessageDelayed(HANDLER_SPLASH, 1500);
        } else {//请求权限方法
            String[] permissionsNew = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(SplashActivity.this, permissionsNew, 1);
        }
    }

    //判断程序是否是第一次运行
    private boolean isFirst() {
        return ShareUtil.getBoolean(this, SHARE_IS_FIRST, true);
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                boolean isAllPermission = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        /*第一次打开App时	false
                        上次弹出权限点击了禁止（但没有勾选“下次不在询问”）	true
                        上次选择禁止并勾选：下次不在询问	false*/
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissions[i]);
                        if (showRequestPermission) {
                            getPermission();//重新申请权限
                            return;
                        } else {
                            /*//已经禁止并不再询问
                            String uriString = UtilTools.getAppPackageName();
                            L.i(TAG,uriString+"??????");
                            Uri packageURI = Uri.parse("package:" + uriString);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);*/
                            Toast.makeText(SplashActivity.this, "应用需要权限，请您手动授予权限", Toast.LENGTH_LONG).show();
                            isAllPermission = false;
                        }
                    } else {

                        //允许权限

                    }
                }
                //授权循环结束,判断是否所以权限都授权
                if (isAllPermission) {
                    L.i(TAG, "所有权限都允许");
                    handler.sendEmptyMessageDelayed(HANDLER_SPLASH, 1500);
                } else {
                    handler.sendEmptyMessageDelayed(HANDLER_EXIT, 1500);
                }
                //delayEntryPage();
                break;
            default:
                break;
        }
    }
}
