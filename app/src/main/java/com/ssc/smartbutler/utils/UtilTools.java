package com.ssc.smartbutler.utils;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.utils
 *  创建者：    SSC
 *  创建时间：   2018/7/7 6:47
 *  描述：     工具统一类
 */

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.ssc.smartbutler.application.BaseApplication;

public class UtilTools {

    //设置字体
    public static void setFont(Context mContext, TextView textView){
        Typeface fontType = Typeface.createFromAsset(mContext.getAssets(), "fonts/FONT.TTF");
        textView.setTypeface(fontType);
    }

    public static String getAppPackageName(){
        ActivityManager activityManager=(ActivityManager) BaseApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        //完整类名
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        String contextActivity = runningActivity.substring(runningActivity.lastIndexOf(".")+1);
        return contextActivity;
    }
}
