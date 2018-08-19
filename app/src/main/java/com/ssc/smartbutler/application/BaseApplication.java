package com.ssc.smartbutler.application;

/*
 *  项目名：    ${PROJECT_NAME}
 *  包名:       ${PACKAGE_NAME}
 *  文件名：    ${NAME}
 *  创建者：    SSC
 *  创建时间：   ${DATE} ${TIME}
 *  描述：     TODO
 */

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.HttpConnectStack;
import com.kymjs.rxvolley.http.RequestQueue;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.service.SmsService;
import com.ssc.smartbutler.ui.SettingActivity;
import com.ssc.smartbutler.utils.ShareUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLSocketFactory;

import cn.bmob.v3.Bmob;

import static com.ssc.smartbutler.utils.StaticClass.BUGLY_ID;
import static com.ssc.smartbutler.utils.StaticClass.BMOB_ID;
import static com.ssc.smartbutler.utils.StaticClass.IS_SMS;
import static com.ssc.smartbutler.utils.StaticClass.TTS_ID;

public class BaseApplication extends Application {

    private static Context context;

    //当前用户
    public static MyUser userInfo = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_ID, true);
        //初始化Bmob
        Bmob.initialize(this, BMOB_ID);
        //SMS Service
        if (ShareUtil.getBoolean(this, IS_SMS,false)){
            startService(new Intent(this, SmsService.class));
        }
        //二维码初始化
        ZXingLibrary.initDisplayOpinion(this);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
    }


    public static Context getContext(){
        return context;
    }


}
