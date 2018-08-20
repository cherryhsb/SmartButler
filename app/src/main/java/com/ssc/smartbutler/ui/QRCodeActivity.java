package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    QRCodeActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/11 15:37
 *  描述：     TODO生成二维码
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.ImageView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayInputStream;

import cn.bmob.v3.BmobUser;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;

public class QRCodeActivity extends BaseActivity {

    private ImageView iv_qrcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        initView();
    }

    private void initView() {
        iv_qrcode = findViewById(R.id.iv_qrcode);
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            //已经登陆显示用户名
            String username = userInfo.getUsername();
            String gender;
            if (userInfo.isGender()) {
                gender = getString(R.string.male);
            } else {
                gender = getString(R.string.female);
            }
            int age = userInfo.getAge();
            String desc = userInfo.getDesc();
            String text = getString(R.string.username) + ":" + username + "\n" +
                    getString(R.string.gender) + ":" + gender + "\n" +
                    getString(R.string.age) + ":" + age + "\n" +
                    getString(R.string.describe) + ":" + desc;
            if (userInfo.getImgString() != null) {
                //利用Base64将String转化为byte数组
                byte[] bytes = Base64.decode(userInfo.getImgString(), Base64.DEFAULT);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                //生成bitmap,显示出来
                Bitmap logo = BitmapFactory.decodeStream(inputStream);
                //带logo
                Bitmap image = CodeUtils.createImage(text, 500, 500, logo);
                iv_qrcode.setImageBitmap(image);
            } else {
                //不带logo
                Bitmap imageNoLogo = CodeUtils.createImage(text, 500, 500, null);
                iv_qrcode.setImageBitmap(imageNoLogo);
            }
        }

    }
}
