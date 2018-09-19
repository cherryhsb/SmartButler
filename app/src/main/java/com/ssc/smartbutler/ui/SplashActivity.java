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
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.ShareUtil;
import com.ssc.smartbutler.utils.UtilTools;

import static com.ssc.smartbutler.utils.StaticClass.INIT_PERMISSION_CODE;
import static com.ssc.smartbutler.utils.StaticClass.SHARE_IS_FIRST;

/*
 * 1.延时2000ms
 * 2.判断程序是否是第一次运行
 * 3.自定义字体
 * 4.Activity全屏主题
 */
public class SplashActivity extends PermissionActivity {

    private static final String TAG = "SplashActivity";

    private TextView tv_splash;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

        initPermission();

    }


    private void initView() {
        tv_splash = findViewById(R.id.tv_splash);


        //设置字体
        UtilTools.setFont(this, tv_splash);


    }


    /**
     * 首次进入,初始化权限
     */
    public void initPermission() {
        if (!hasPermission(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )) {
            requestPermission(INIT_PERMISSION_CODE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    disposePermission();
                }
            }, 1500);
        }
    }

    @Override
    public void disposePermission() {
        if (isFirst()) {
            ShareUtil.putBoolean(SplashActivity.this, SHARE_IS_FIRST, false);
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
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

}
