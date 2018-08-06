package com.ssc.smartbutler.utils;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.utils
 *  创建者：    SSC
 *  创建时间：   2018/7/8 19:40
 *  描述：     Log封装类
 */

import android.util.Log;

public class L {

    //开关
    public static final boolean DEBUG = true;
    //TAG
    //public static final String TAG = "SmartButler";

    //五个等级 DIWEF

    public static void d(String tag,String msg){
        if (DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if (DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if (DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void e(String tag,String msg){
        if (DEBUG){
            Log.e(tag,msg);
        }
    }
}
