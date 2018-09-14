package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    BaseActivity
 *  创建者：    SSC
 *  创建时间：   2018/7/7 0:43
 *  描述：     Activity基类
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
import android.view.MenuItem;
import android.widget.Toast;

import com.ssc.smartbutler.utils.StaticClass;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要做的事情：
 * 1.统一的属性
 * 2.统一的接口
 * 3.统一的方法
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
                getSupportActionBar().setElevation(0);
        }

        //ActionBar显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


}
