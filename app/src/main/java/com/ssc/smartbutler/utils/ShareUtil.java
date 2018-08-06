package com.ssc.smartbutler.utils;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.utils
 *  创建者：    SSC
 *  创建时间：   2018/7/8 20:16
 *  描述：     SharedPreferences封装
 */

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtil {

    public static final String NAME = "config";


    //存储
    public static void putInt(Context mContext, String key, int value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putString(Context mContext, String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putBoolean(Context mContext, String key, boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //读取
    public static int getInt(Context mContext, String key, int deValue) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).getInt(key, deValue);
    }

    public static String getString(Context mContext, String key, String deValue) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(key, deValue);
    }

    public static boolean getBoolean(Context mContext, String key, boolean deValue) {
        return mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).getBoolean(key, deValue);
    }

    //删除单个
    public static void delShare(Context mContext,String key){
        mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().remove(key).apply();
    }

    //删除全部
    public static void delAll(Context mContext){
        mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
