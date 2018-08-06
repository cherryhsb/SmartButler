package com.ssc.smartbutler.fragment;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.fragment
 *  创建者：    SSC
 *  创建时间：   2018/7/8 3:04
 *  描述：     TODO
 */

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.ui.LoginActivity;
import com.ssc.smartbutler.ui.UserInfoActivity;

import java.io.ByteArrayInputStream;

import cn.bmob.v3.BmobUser;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;
import static com.ssc.smartbutler.utils.StaticClass.REQUEST_CODE_LOGIN;

public class UserFragment extends Fragment implements View.OnClickListener {

    private LinearLayout ll_user_login,ll_user_info;

    private TextView tv_user_name;

    private String username;

    private ImageView iv_user_icon;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        ll_user_login = (LinearLayout) getActivity().findViewById(R.id.ll_user_login);
        ll_user_login.setOnClickListener(this);
        ll_user_info = (LinearLayout) getActivity().findViewById(R.id.ll_user_info);
        ll_user_info.setOnClickListener(this);
        tv_user_name = (TextView) getActivity().findViewById(R.id.tv_user_name);
        iv_user_icon = getActivity().findViewById(R.id.iv_user_icon);

        //当前用户
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo != null){
            //已经登陆显示用户名
            username = userInfo.getUsername();
            tv_user_name.setText(username);
            if (userInfo.getImgString()!=null){
                //利用Base64将String转化为byte数组
                byte[] bytes = Base64.decode(userInfo.getImgString(),Base64.DEFAULT);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                //生成bitmap,显示出来
                iv_user_icon.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            }
            ll_user_login.setVisibility(View.GONE);
            ll_user_info.setVisibility(View.VISIBLE);
        }else{
            //缓存用户对象为空时，显示需要登陆
            tv_user_name.setText("");
            ll_user_login.setVisibility(View.VISIBLE);
            ll_user_info.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_user_login:
                startActivityForResult(new Intent(getActivity(), LoginActivity.class),REQUEST_CODE_LOGIN);
                //getActivity().finish();
                break;
            case R.id.ll_user_info:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                //getActivity().finish();
                break;

        }
    }
}
