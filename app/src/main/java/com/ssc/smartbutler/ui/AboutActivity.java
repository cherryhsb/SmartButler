package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    AboutActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/20 2:43
 *  描述：     关于
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.StaticClass;
import com.ssc.smartbutler.utils.UtilTools;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AboutActivity";

    private TextView tv_show,tv_version,tv_app_name;

    private Button btn_update;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView() {
        tv_show = findViewById(R.id.tv_show);
        tv_version = findViewById(R.id.tv_version);
        tv_app_name = findViewById(R.id.tv_app_name);
        btn_update = findViewById(R.id.btn_update);

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
    private void getVersionNameCode()  {
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
        switch (v.getId()){
            case R.id.btn_update:
                /*
                * 步骤:
                * 1.请求服务器的配置文件,拿到versionCode
                * 2.比较
                * 3.dialog提示
                * 4.跳转到更新界面,并把url传递过去
                * */
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        //L.i(TAG,t);
                        parsingJson(t);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                        Toast.makeText(AboutActivity.this, getString(R.string.unable_connect_server),Toast.LENGTH_SHORT).show();
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
            if (versionCodeUpdate >versionCode){
                showUpdateDialog(versionNameUpdate+"\n"+content);
            }else {
                Toast.makeText(this, getString(R.string.is_latest_version),Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.new_version));
        builder.setMessage(content);
        builder.setPositiveButton(getString(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AboutActivity.this, UpdateActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
