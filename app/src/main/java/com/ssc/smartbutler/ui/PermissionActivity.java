package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    PermissionActivity
 *  创建者：    SSC
 *  创建时间：   2018/9/14 23:34
 *  描述：     TODO
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ssc.smartbutler.service.SmsService;
import com.ssc.smartbutler.utils.StaticClass;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 为子类提供一个权限检查方法
     *
     * @param permissions
     * @return
     */
    public boolean hasPermission(String... permissions) {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * 为子类提供一个权限请求方法
     *
     * @param requestCode
     * @param permissions
     */
    public void requestPermission(int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> deniedPermissions = new ArrayList<>();
        switch (requestCode) {
            case StaticClass.INIT_PERMISSION_CODE:
                boolean again = false;  //是否再次弹出申请权限
                boolean permissionAll = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//上次弹出权限点击了禁止
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            //上次弹出权限点击了禁止（但没有勾选“下次不在询问”）
                            again = true;
                        } else {//上次选择禁止并勾选：下次不在询问
                            permissionAll = false;
                            deniedPermissions.add(permissions[i]);
                        }
                    } else {//上次弹出权限点击了允许

                    }
                }
                if (again) {//再次弹出申请权限
                    requestPermission(requestCode, permissions);
                } else {//不再弹出申请权限
                    if (permissionAll) {//勾选允许所有权限
                        disposePermission();
                    } else {//禁止了权限
                        showDeniedDialog(deniedPermissions);
                    }
                }
                break;
            case StaticClass.CAMERA_CODE:
                requestPermission(grantResults[0],permissions[0],requestCode,"相机权限");
                break;
            case StaticClass.SMS_CODE:
                requestPermission(grantResults[0],permissions[0],requestCode,"短信权限");

                break;
            default:
                break;
        }
    }

    public boolean requestPermission(int grantResult, String permission, int requestCode,String hint){
        if (grantResult != PackageManager.PERMISSION_GRANTED) {//不允许才会执行此方法
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //上次弹出权限点击了禁止（但没有勾选“下次不在询问”）
                requestPermission(requestCode, permission);
            } else {
                //上次选择禁止并勾选：下次不在询问
                showDeniedToast(hint);
            }
            return false;
        } else {//允许了权限
            return true;
        }
    }


    /**
     * 拒绝权限toast
     *
     * @param
     */
    private void showDeniedToast(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strings) {
            stringBuilder.append(s).append("\n");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        Toast.makeText(this, "您拒绝了" + "\n" + stringBuilder, Toast.LENGTH_SHORT).show();
    }

    /**
     * 拒绝权限对话框
     *
     * @param deniedPermissions
     */
    public void showDeniedDialog(List<String> deniedPermissions) {
        List<String> stringList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (!deniedPermissions.isEmpty()) {
            for (String s : deniedPermissions) {
                switch (s) {
                    case Manifest.permission.READ_PHONE_STATE:
                        stringList.add("手机/电话权限");
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        stringList.add("存储权限");
                        break;
                    default:
                        break;
                }
            }
        }
        for (String s : stringList) {
            //Log.i(TAG, "getDeniedPermissions: " + s);
            stringBuilder.append(s).append("\n");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("需要下列权限才可正常使用")
                .setMessage(stringBuilder)
                .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("不开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    /**
     * 默认拒绝初始化权限处理
     */
    public void disposePermission() {

    }

    public void startSMSService() {

    }
}
