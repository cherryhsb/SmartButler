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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.L;

public class UpdateActivity extends BaseActivity {

    private static final String TAG = "UpdateActivity";

    private TextView tv_size_update;

    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView() {
        tv_size_update = findViewById(R.id. tv_size_update);

        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";

        //下载
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        //if (url!=null)
        RxVolley.download(path, url, new ProgressListener() {
            @Override
            public void onProgress(long transferredBytes, long totalSize) {
                L.i(TAG, transferredBytes + totalSize + "");
            }
        }, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                //L.i(TAG,"成功");
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                //L.i(TAG,"失败");
            }
        });

    }
}
