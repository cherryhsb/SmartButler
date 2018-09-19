package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    UpdateActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/20 5:32
 *  描述：     更新界面
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.L;

import java.io.File;

public class UpdateActivity extends BaseActivity {

    private static final String TAG = "UpdateActivity";

    //正在下载
    public static final int HANDLER_LODING = 10001;
    //下载完成
    public static final int HANDLER_OK = 10002;
    //下载失败
    public static final int HANDLER_ON = 10003;

    private TextView tv_size_update,tv_not_leave;

    private String path, url;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_LODING:
                    //实时更新进度
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size_update.setText(transferredBytes+" / "+totalSize);
                    tv_not_leave.setVisibility(View.VISIBLE);
                    break;
                case HANDLER_OK:
                    tv_size_update.setText("下载成功");
                    tv_not_leave.setText("到"+path+"安装");
                    tv_not_leave.setVisibility(View.VISIBLE);
                    //安装这个apk
                    //startInstallApk();
                    break;
                case HANDLER_ON:
                    tv_size_update.setText("下载失败");
                    tv_not_leave.setVisibility(View.GONE);
                    break;
            }
        }
    };

    //安装apk
    private void startInstallApk() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView() {
        tv_size_update = findViewById(R.id.tv_size_update);
        tv_not_leave = findViewById(R.id.tv_not_leave);

        Intent intent = getIntent();

        //path = FileUtils.getSDCardPath() + "/"+"SmartButler/"+ System.currentTimeMillis() + ".apk";
        path = FileUtils.getSDCardPath() + "/"+"SmartButler/"+ intent.getStringExtra("versionNameUpdate") + ".apk";

        //下载
        url = intent.getStringExtra("url");
        //if (url!=null)
        RxVolley.download(path, url, new ProgressListener() {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                L.i(TAG, transferredBytes + totalSize + "");
                Message message = new Message();
                message.what = HANDLER_LODING;
                Bundle bundle = new Bundle();
                bundle.putLong("transferredBytes",transferredBytes);
                bundle.putLong("totalSize",totalSize);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                L.i(TAG, "成功");
                Message message = new Message();
                message.what = HANDLER_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                L.i(TAG, "失败");
                Message message = new Message();
                message.what = HANDLER_ON;
                handler.sendMessage(message);
            }
        });

    }
}
