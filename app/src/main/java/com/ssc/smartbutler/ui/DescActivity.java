package com.ssc.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.utils.L;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/12 9:43
 *  描述：     设置简介
 */

public class DescActivity extends BaseActivity{

    private static final String TAG = "DescActivity";

    private EditText et_desc;

    private TextView tv_desc_save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);
        et_desc = (EditText) findViewById(R.id.et_desc);
        tv_desc_save = (TextView) findViewById(R.id.tv_desc_save);

        et_desc.setText(getIntent().getStringExtra("desc"));

        tv_desc_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*L.i(TAG,et_desc.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("desc_return",et_desc.getText().toString());
                setResult(RESULT_OK,intent);*/

                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                // 修改用户的邮箱为xxx@163.com
                bmobUser.setDesc(et_desc.getText().toString());
                bmobUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //toast("更新用户信息成功");
                            L.i(TAG,et_desc.getText().toString());
                            Intent intent = new Intent();
                            intent.putExtra("desc_return",et_desc.getText().toString());
                            setResult(RESULT_OK,intent);
                            finish();
                        } else {
                            //toast("更新用户信息失败:" + e.getMessage());
                            Toast.makeText(DescActivity.this, getString(R.string.update_user_information_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
