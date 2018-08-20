package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/10 21:47
 *  描述：     登录
 */

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssc.smartbutler.MainActivity;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.utils.IEditTextChangeListener;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;
import com.ssc.smartbutler.utils.WorksSizeCheckUtil;
import com.ssc.smartbutler.view.CustomDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private EditText et_login_username, et_login_password;

    private Button btn_login;

    private TextView tv_login_forget, tv_login_register, tv_login_enter;

    private CustomDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        et_login_username = (EditText) findViewById(R.id.et_login_username);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_login_forget = (TextView) findViewById(R.id.tv_login_forget);
        tv_login_register = (TextView) findViewById(R.id.tv_login_register);
        tv_login_enter = (TextView) findViewById(R.id.tv_login_enter);

        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        tv_login_enter.setOnClickListener(this);
        tv_login_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        tv_login_forget.setOnClickListener(this);



        //设置button是否可以点击
        setButtonEnabled();
    }

    private void setButtonEnabled() {
        //1.创建工具类对象 把要改变颜色的tv先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(btn_login);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(et_login_username, et_login_password);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 tv 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    btn_login.setAlpha(1);
                    btn_login.setEnabled(true);
                } else {
                    btn_login.setAlpha(0.5f);
                    btn_login.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_enter:
                //startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.btn_login:
                String username = et_login_username.getText().toString().trim();
                String password = et_login_password.getText().toString().trim();
                dialog.show();
                //登录
                MyUser user = new MyUser();
                user.setUsername(username);
                user.setPassword(password);
                user.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        dialog.dismiss();
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                            //finish();
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                            userInfo = BmobUser.getCurrentUser(MyUser.class);
                            finish();
                        } else {
                            L.i(TAG, e.toString());
                            Toast.makeText(LoginActivity.this, getString(R.string.login_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.tv_login_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

/*    BmobUser bu2 = new BmobUser();
        bu2.setUsername("lucky");
        bu2.setPassword("123456");
        bu2.login(new SaveListener<BmobUser>() {

        @Override
        public void done(BmobUser bmobUser, BmobException e) {
            if(e==null){
                toast("登录成功:");
                //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
            }else{
                loge(e);
            }
        }
    });*/
}
