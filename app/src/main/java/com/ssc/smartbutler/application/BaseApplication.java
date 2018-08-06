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

import com.ssc.smartbutler.entity.MyUser;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

import static com.ssc.smartbutler.utils.StaticClass.BUGLY_ID;
import static com.ssc.smartbutler.utils.StaticClass.BMOB_ID;

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
    }

    public static Context getContext(){
        return context;
    }
}
