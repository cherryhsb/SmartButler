package com.ssc.smartbutler.activity;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    QRCodeActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/11 15:37
 *  描述：     TODO生成二维码
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.ScreenUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.bmob.v3.datatype.BmobFile;

import static com.ssc.smartbutler.application.BaseApplication.getContext;
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
        //userInfo = BmobUser.getCurrentUser(MyUser.class);
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

            BmobFile icon = userInfo.getIcon();
            //L.i(TAG, icon.getUrl()+"hahaha");
            int i = ScreenUtils.getScreenWidth(this) * 2 / 3;
            if (icon != null) {//设置过头像
                //带logo
                String iconCompressPath = getContext().getExternalFilesDir(userInfo.getUsername()).getAbsolutePath() + "/icon/" + userInfo.getUsername() + "(compress).jpg";
                Bitmap logo = BitmapFactory.decodeFile(iconCompressPath);
                Bitmap image = CodeUtils.createImage(text, i, i, logo);
                iv_qrcode.setImageBitmap(image);
            } else {//没有设置过头像,显示默认图标
                //不带logo
                Bitmap imageNoLogo = CodeUtils.createImage(text, i, i, null);
                iv_qrcode.setImageBitmap(imageNoLogo);
            }
        } else {
            //缓存用户对象为空
        }
    }
}
