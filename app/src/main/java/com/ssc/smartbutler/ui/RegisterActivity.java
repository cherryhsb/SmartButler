package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/11 3:19
 *  描述：     注册
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.utils.IEditTextChangeListener;
import com.ssc.smartbutler.utils.WorksSizeCheckUtil;
import com.ssc.smartbutler.view.CustomDialog;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";

    private EditText et_register_username, et_register_password, et_register_password_again, et_register_email;

    private Button btn_register;

    private TextView tv_register_login, tv_register_enter;

    private CustomDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        et_register_username = (EditText) findViewById(R.id.et_register_username);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_password_again = (EditText) findViewById(R.id.et_register_password_again);
        et_register_email = (EditText) findViewById(R.id.et_register_email);
        tv_register_login = (TextView) findViewById(R.id.tv_register_login);
        tv_register_enter = (TextView) findViewById(R.id.tv_register_enter);
        btn_register = (Button) findViewById(R.id.btn_register);

        dialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        tv_register_enter.setOnClickListener(this);
        tv_register_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        setButtonEnabled();
    }

    private void setButtonEnabled() {
        //1.创建工具类对象 把要改变颜色的tv先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(btn_register);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(et_register_username, et_register_password, et_register_password_again, et_register_email);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 tv 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    btn_register.setAlpha(1);
                    btn_register.setEnabled(true);
                } else {
                    btn_register.setAlpha(0.5f);
                    btn_register.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register_enter:
                //startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.tv_register_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_register:
                //获取到输入框的值
                String username = et_register_username.getText().toString().trim();
                String password = et_register_password.getText().toString().trim();
                String passwordAgain = et_register_password_again.getText().toString().trim();
                String email = et_register_email.getText().toString().trim();

                if (password.equals(passwordAgain)) {
                    if (password.length() >= 6 && password.length() <= 16) {
                        dialog.show();
                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(username);
                        user.setNickname(username);
                        user.setPassword(password);
                        user.setEmail(email);
                            /*user.setAge(0);
                            user.setGender(Boolean.parseBoolean(null));
                            */
                        user.setDesc("这个人很懒什么都没有留下");
                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                dialog.dismiss();
                                if (e == null) {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.registered_successfully), Toast.LENGTH_SHORT).show();
                                    userInfo = BmobUser.getCurrentUser(MyUser.class);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.registered_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, getString(R.string.password_rule), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.two_passwords_not_match), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

/*      BmobUser bu = new BmobUser();
        bu.setUsername("sendi");
        bu.setPassword("123456");
        bu.setEmail("sendi@163.com");
        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<MyUser>()

        {
            @Override
            public void done (MyUser s, BmobException e){
            if (e == null) {
                toast("注册成功:" + s.toString());
            } else {
                loge(e);
            }
        }
        });*/
}
