package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    WebViewActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/4 9:30
 *  描述：     TODO:新闻详情
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.L;

public class WebViewActivity extends BaseActivity {

    private ProgressBar pb_web;

    private WebView wv_web;

    private static final String TAG = "WebViewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initView();
    }

    private void initView() {
        pb_web = findViewById(R.id.pb_web);
        wv_web = findViewById(R.id.wv_web);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String url = intent.getStringExtra("url");
        L.i(TAG,url);
        //设置标题
        getSupportActionBar().setTitle(title);

        //加载网页
        //支持js
        wv_web.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        wv_web.getSettings().setSupportZoom(true);
        //控制器
        wv_web.getSettings().setBuiltInZoomControls(true);
        //本地的接口.接口回调
        wv_web.setWebChromeClient(new webViewChromeClient());
        //加载
        wv_web.loadUrl(url);

        //本地显示
        wv_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                //我接受这个事件
                return true;
            }
        });
    }

    public class webViewChromeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress==100){
                pb_web.setVisibility(View.GONE);
            }
            pb_web.setProgress(newProgress);
        }
    }
}
