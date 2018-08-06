package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/8 6:32
 *  描述：     设置
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.ssc.smartbutler.MainActivity;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;

import cn.bmob.v3.BmobUser;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;
import static com.ssc.smartbutler.utils.StaticClass.REQUEST_CODE_EXIT;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_setting_exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        btn_setting_exit = (Button) findViewById(R.id.btn_setting_exit);

        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo != null){
            btn_setting_exit.setVisibility(View.VISIBLE);
        }else {
            btn_setting_exit.setVisibility(View.GONE);
        }
        btn_setting_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_setting_exit:
                //退出登录
                BmobUser.logOut();   //清除缓存用户对象
                userInfo = BmobUser.getCurrentUser(MyUser.class); // 现在的currentUser是null了
                //startActivity(new Intent(this, MainActivity.class));
                finish();
                /*tv_user_name.setText("");
                ll_user_login.setVisibility(View.VISIBLE);
                ll_user_info.setVisibility(View.GONE);*/
                break;
        }

    }
}
