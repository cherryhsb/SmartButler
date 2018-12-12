package com.ssc.smartbutler.activity;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/12 9:42
 *  描述：     忘记密码
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.IEditTextChangeListener;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.WorksSizeCheckUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ForgetPasswordActivity";

    private EditText et_forget_email;

    private Button btn_forget_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
    }

    private void initView() {
        et_forget_email = (EditText) findViewById(R.id.et_forget_email);
        btn_forget_password = (Button) findViewById(R.id.btn_forget_password);
        btn_forget_password.setOnClickListener(this);

        setButtonEnabled();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_forget_password:
                final String email = et_forget_email.getText().toString().trim();
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(ForgetPasswordActivity.this,
                                    getString(R.string.reset_password_success) + email + getString(R.string.reset_operation),Toast.LENGTH_SHORT).show();
                        }else{
                            L.i(TAG, e.toString());
                            Toast.makeText(ForgetPasswordActivity.this, getString(R.string.failure) + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

/*    final String email = "xxx@163.com";
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {

        @Override
        public void done(BmobException e) {
            if(e==null){
                toast("重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
            }else{
                toast("失败:" + e.getMessage());
            }
        }
    });*/

    private void setButtonEnabled() {
        //1.创建工具类对象 把要改变颜色的tv先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(btn_forget_password);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(et_forget_email);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 tv 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    btn_forget_password.setAlpha(1);
                    btn_forget_password.setEnabled(true);
                } else {
                    btn_forget_password.setAlpha(0.5f);
                    btn_forget_password.setEnabled(false);
                }
            }
        });
    }
}
