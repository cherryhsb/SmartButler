package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/8 6:32
 *  描述：     设置
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.service.SmsService;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;
import com.ssc.smartbutler.utils.StaticClass;
import com.ssc.smartbutler.utils.SystemUtil;

import cn.bmob.v3.BmobUser;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;
import static com.ssc.smartbutler.utils.StaticClass.IS_SMS;
import static com.ssc.smartbutler.utils.StaticClass.IS_TTS;
import static com.ssc.smartbutler.utils.StaticClass.SHARE_KNOW_MIUI_SMS;

public class SettingActivity extends PermissionActivity implements View.OnClickListener {

    private static final String TAG = "SettingActivity";

    private Switch switch_tts, switch_sms;

    private Button btn_setting_exit;

    private LinearLayout ll_about;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }

        //ActionBar显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        btn_setting_exit = findViewById(R.id.btn_setting_exit);
        switch_tts = findViewById(R.id.switch_tts);
        switch_sms = findViewById(R.id.switch_sms);
        ll_about = findViewById(R.id.ll_about);

        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            btn_setting_exit.setVisibility(View.VISIBLE);
        } else {
            btn_setting_exit.setVisibility(View.GONE);
        }
        btn_setting_exit.setOnClickListener(this);

        switch_tts.setChecked(ShareUtil.getBoolean(this, IS_TTS, false));
        switch_tts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                ShareUtil.putBoolean(SettingActivity.this, IS_TTS, isChecked);
            }
        });
        switch_sms.setChecked(ShareUtil.getBoolean(this, IS_SMS, false));
        switch_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.setChecked(isChecked);
                ShareUtil.putBoolean(SettingActivity.this, IS_SMS, isChecked);
                if (isChecked) {
                    if (SystemUtil.getSystem().equals(SystemUtil.SYS_MIUI)) {
                        if (ShareUtil.getBoolean(SettingActivity.this, SHARE_KNOW_MIUI_SMS, true)) {
                            L.i(TAG, "onCheckedChanged: dialog");
                            showDialog();
                        }
                    }
                    if (!hasPermission(Manifest.permission.RECEIVE_SMS)) {//如果没有权限
                        requestPermission(StaticClass.SMS_CODE, Manifest.permission.RECEIVE_SMS);
                        buttonView.setChecked(false);
                        L.i(TAG, "onCheckedChanged: 没有权限");
                    } else {
                        startSMSService();
                    }
                } else {
                    L.i(TAG, "onCheckedChanged: 关闭了服务");
                    stopService(new Intent(SettingActivity.this, SmsService.class));
                }
            }
        });

        ll_about.setOnClickListener(this);

        try {
            getVersionNameCode();
            L.i(TAG, versionName + "-----" + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    //ActionBar菜单栏返回键操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handlePermission(int grantResult, String permission, int requestCode, String hint) {
        if (super.handlePermission(grantResult, permission, requestCode, hint)){
            startSMSService();
            switch_sms.setChecked(true);
            return true;
        }else {
            return false;
        }
    }

    public void startSMSService() {
        startService(new Intent(SettingActivity.this, SmsService.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setting_exit:
                //退出登录
                BmobUser.logOut();   //清除缓存用户对象
                userInfo = BmobUser.getCurrentUser(MyUser.class); // 现在的currentUser是null了
                L.i(TAG, userInfo+"");
                finish();
                break;
            case R.id.ll_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

    }

    private String versionName;
    private int versionCode;

    //获取当前版本号
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        versionName = packageInfo.versionName;
        versionCode = packageInfo.versionCode;
    }


    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * @return whether or not is MIUI
     * @link http://dev.xiaomi.com/doc/p=254/index.html
     */
    /*public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        L.i(TAG, "Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(new File(Environment
                        .getRootDirectory(), "build.prop")));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } else {
            return false;
        }
    }*/

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.Xiaomi_SMS_hint))
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(getString(R.string.never_prompt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShareUtil.putBoolean(SettingActivity.this, SHARE_KNOW_MIUI_SMS, false);
                    }
                })
                .setCancelable(false)
                .show();
    }

}
