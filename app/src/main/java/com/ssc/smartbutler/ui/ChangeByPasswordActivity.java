package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/12 14:53
 *  描述：     TODO
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ssc.smartbutler.MainActivity;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.utils.ActivityManager;
import com.ssc.smartbutler.utils.L;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;

public class ChangeByPasswordActivity extends BaseActivity {

    private static final String TAG = "ChangeByPasswordActivit";

    private EditText et_old_password, et_new_password, et_new_password_again;

    private Button btn_change_old_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_by_password);

        initView();

        ActivityManager.getInstance().addActivity(this);
    }

    private void initView() {
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_new_password_again = (EditText) findViewById(R.id.et_new_password_again);
        btn_change_old_password = (Button) findViewById(R.id.btn_change_old_password);

        btn_change_old_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordOld = et_old_password.getText().toString().trim();
                String passwordNew = et_new_password.getText().toString().trim();
                String passwordNewAgain = et_new_password_again.getText().toString().trim();

                if (!TextUtils.isEmpty(passwordOld) && !TextUtils.isEmpty(passwordNew) &&
                        !TextUtils.isEmpty(passwordNewAgain)) {
                    if (passwordNew.equals(passwordNewAgain)) {
                        if (passwordNew.length() >= 6 && passwordNew.length() <= 16) {
                            //修改密码
                            BmobUser.updateCurrentUserPassword(passwordOld, passwordNew, new UpdateListener() {

                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(ChangeByPasswordActivity.this, "密码修改成功，可以用新密码进行登录啦", Toast.LENGTH_SHORT).show();
                                        BmobUser.logOut();   //清除缓存用户对象
                                        userInfo = BmobUser.getCurrentUser(MyUser.class); // 现在的currentUser是null了
                                        Intent intent = new Intent(ChangeByPasswordActivity.this, MainActivity.class);
                                        intent.putExtra("page",3);
                                        startActivity(intent);
                                        ActivityManager.getInstance().exit();
                                        //finish();
                                    }else{
                                        Toast.makeText(ChangeByPasswordActivity.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }else {
                            Toast.makeText(ChangeByPasswordActivity.this, "密码必须是6-16位的", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangeByPasswordActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangeByPasswordActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
