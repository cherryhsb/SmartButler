package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/12 9:43
 *  描述：     重置密码
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.ActivityManager;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_by_password,tv_by_email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();

        ActivityManager.getInstance().addActivity(this);
    }

    private void initView() {
        tv_by_password = (TextView) findViewById(R.id.tv_by_password);
        tv_by_email = (TextView) findViewById(R.id.tv_by_email);
        tv_by_password.setOnClickListener(this);
        tv_by_email.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_by_password:
                startActivity(new Intent(this, ChangeByPasswordActivity.class));
                break;
            case R.id.tv_by_email:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }

    }
}
