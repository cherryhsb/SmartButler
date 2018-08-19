package com.ssc.smartbutler.fragment;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.fragment
 *  创建者：    SSC
 *  创建时间：   2018/7/8 3:04
 *  描述：     TODO
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.ui.ExpressActivity;
import com.ssc.smartbutler.ui.AttributionActivity;
import com.ssc.smartbutler.ui.LbsActivity;
import com.ssc.smartbutler.ui.LoginActivity;
import com.ssc.smartbutler.ui.QRCodeActivity;
import com.ssc.smartbutler.ui.ScanActivity;
import com.ssc.smartbutler.ui.UserInfoActivity;
import com.ssc.smartbutler.utils.L;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayInputStream;

import cn.bmob.v3.BmobUser;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;
import static com.ssc.smartbutler.utils.StaticClass.REQUEST_CODE_LOGIN;
import static com.ssc.smartbutler.utils.StaticClass.SCAN_REQUEST_CODE;

public class UserFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "UserFragment";

    private LinearLayout ll_user_login,ll_user_info;

    private TextView tv_user_name,tv_info_express, tv_attribution,tv_user_scan,tv_lbs;

    private String username;

    private ImageView iv_user_icon,iv_user_QRcode;


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
        tv_info_express = getActivity().findViewById(R.id.tv_user_express);
        tv_attribution = getActivity().findViewById(R.id.tv_user_attribution);
        tv_user_scan = getActivity().findViewById(R.id.tv_user_scan);
        tv_lbs = getActivity().findViewById(R.id.tv_lbs);
        iv_user_QRcode = getActivity().findViewById(R.id.iv_user_qrcode);

        tv_info_express.setOnClickListener(this);
        tv_attribution.setOnClickListener(this);
        tv_user_scan.setOnClickListener(this);
        tv_lbs.setOnClickListener(this);
        iv_user_QRcode.setOnClickListener(this);

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
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                iv_user_icon.setImageBitmap(bitmap);
            }else {
                iv_user_icon.setImageResource(R.drawable.user);
            }
            ll_user_login.setVisibility(View.INVISIBLE);
            ll_user_info.setVisibility(View.VISIBLE);
        }else{
            //缓存用户对象为空时，显示需要登陆
            tv_user_name.setText("");
            ll_user_login.setVisibility(View.VISIBLE);
            ll_user_info.setVisibility(View.GONE);
        }

        /*//自定义二维码扫描
        *//**
         * 执行扫面Fragment的初始化操作
         *//*
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.activity_scan);

        *//**
         * 二维码解析回调函数
         *//*
        CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                bundle.putString(CodeUtils.RESULT_STRING, result);
                resultIntent.putExtras(bundle);
                SecondActivity.this.setResult(RESULT_OK, resultIntent);
                SecondActivity.this.finish();
            }

            @Override
            public void onAnalyzeFailed() {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "");
                resultIntent.putExtras(bundle);
                SecondActivity.this.setResult(RESULT_OK, resultIntent);
                SecondActivity.this.finish();
            }
        };

        captureFragment.setAnalyzeCallback(analyzeCallback);
        *//**
         * 替换我们的扫描控件
         *//*
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();*/
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
            case R.id.tv_user_express:
                startActivity(new Intent(getActivity(),ExpressActivity.class));
                break;
            case R.id.tv_user_attribution:
                startActivity(new Intent(getActivity(),AttributionActivity.class));
                break;
            case R.id.tv_user_scan:
                //startActivityForResult(new Intent(getActivity(), CaptureActivity.class), SCAN_REQUEST_CODE);
                startActivityForResult(new Intent(getActivity(), ScanActivity.class), SCAN_REQUEST_CODE);
                break;
            case R.id.iv_user_qrcode:
                startActivity(new Intent(getActivity(), QRCodeActivity.class));
                break;
            case R.id.tv_lbs:
                startActivity(new Intent(getActivity(), LbsActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.i(TAG,requestCode+"");
        //super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        L.i(TAG,(null == data) +"");
        if (requestCode == SCAN_REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    //L.i(TAG,result);
                    //Toast.makeText(getActivity(), "解析结果:" +"\n"+ result, Toast.LENGTH_LONG).show();
                    showDialog(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showDialog(String result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("解析结果:");
        builder.setMessage(result);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        /*builder.setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShareUtil.putBoolean(AttributionActivity.this,SHARE_IS_HINT_LOCATION,false);
            }
        });*/
        builder.show();
    }
}
