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
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.service.SmsService;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

import static com.ssc.smartbutler.utils.StaticClass.BMOB_ID;
import static com.ssc.smartbutler.utils.StaticClass.BUGLY_ID;
import static com.ssc.smartbutler.utils.StaticClass.IS_SMS;

public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";

    private static Context context;

    //当前用户
    public static MyUser userInfo = null;

    public static String iconCompressPath;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
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

        //初始化百度地图
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

        userInfo = BmobUser.getCurrentUser(MyUser.class);
        L.i(TAG, "application");
        L.i(TAG, "application"+userInfo);
        if (userInfo!=null){
            L.i(TAG, "application"+iconCompressPath);
            iconCompressPath =context.getExternalFilesDir(userInfo.getUsername()).getAbsolutePath()+ "/icon/" + userInfo.getUsername() + "(compress).jpg";
        }
    }


    public static Context getContext(){
        return context;
    }


}
