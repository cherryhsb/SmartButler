package com.ssc.smartbutler.activity;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    AboutActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/20 2:43
 *  描述：     关于
 */

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.download.DownloadService;
import com.ssc.smartbutler.utils.StaticClass;
import com.ssc.smartbutler.utils.UtilTools;
import com.ssc.smartbutler.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.ssc.smartbutler.utils.StaticClass.INSTALL_PACKAGES_REQUESTCODE;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AboutActivity";

    private TextView tv_show, tv_version, tv_app_name;

    private Button btn_update;

    private CustomDialog dialogProgress;

    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();

        //启动并绑定service
        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        //打开activity,启动并绑定
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    private void initView() {
        tv_show = findViewById(R.id.tv_show);
        tv_version = findViewById(R.id.tv_version);
        tv_app_name = findViewById(R.id.tv_app_name);
        btn_update = findViewById(R.id.btn_update);

        dialogProgress = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialogProgress.setCancelable(false);

        tv_app_name.setText(getString(R.string.app_name));
        getVersionNameCode();
        tv_version.setText(versionName);
        tv_show.setText("挥舞着键盘和本子，\n发誓要把世界写个明明白白。");
        //设置字体
        UtilTools.setFont(this, tv_show);

        btn_update.setOnClickListener(this);
    }

    private String versionName;
    private int versionCode;

    //获取当前版本号
    private void getVersionNameCode() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = getString(R.string.unknown);
            versionCode = 0;
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                /*
                 * 步骤:
                 * 1.请求服务器的配置文件,拿到versionCode
                 * 2.比较
                 * 3.dialog提示
                 * 4.跳转到更新界面,并把url传递过去
                 * */
                dialogProgress.show();
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        dialogProgress.dismiss();
                        //L.i(TAG,t);
                        parsingJson(t);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        dialogProgress.dismiss();
                        Toast.makeText(AboutActivity.this, getString(R.string.unable_connect_server), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private int versionCodeUpdate;
    private String versionNameUpdate;
    private String url;
    private String content;

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            versionCodeUpdate = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            versionNameUpdate = jsonObject.getString("versionName");
            content = jsonObject.getString("content");
            if (versionCodeUpdate > versionCode) {
                showUpdateDialog(versionNameUpdate + "\n" + content);
            } else {
                Toast.makeText(this, getString(R.string.is_latest_version), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_version));
        builder.setMessage(content + "\n"+"\n"
                +"Android 8.0请开启安装权限");
        builder.setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*Intent intent = new Intent(AboutActivity.this, UpdateActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("versionNameUpdate", versionNameUpdate);
                startActivity(intent);*/
                downloadBinder.startDownload(url);
                /*Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!getPackageManager().canRequestPackageInstalls()){
                        //ActivityCompat.requestPermissions(AboutActivity.this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出activity解绑
        unbindService(connection);
        //stopService(new Intent(this, DownloadService.class));
    }

    //启动安装
    private void startInstallApk() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        String path = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).getPath() + url.substring(url.lastIndexOf("/")) + ".apk";//内置sd卡的Download文件夹下
        i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(i);
        finish();
    }

    public void install(Context context) {
        String path = Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).getPath() + url.substring(url.lastIndexOf("/")) + ".apk";//内置sd卡的Download文件夹下
        File file = new File(path);
        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(context, "com.ssc.smartbutler.fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 如果为8.0以上系统，则判断是否有未知应用安装权限
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO() {
        return getPackageManager().canRequestPackageInstalls();
    }

    /**
     * 开启设置安装未知来源应用权限界面
     * @param context
     */
    /*@RequiresApi (api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        startActivityForResult(intent,REQUEST_CODE_APP_INSTALL);
    }*/
}
