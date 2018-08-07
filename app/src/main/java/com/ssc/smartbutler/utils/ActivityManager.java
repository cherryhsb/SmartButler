package com.ssc.smartbutler.utils;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.utils
 *  文件名：    ActivityManager
 *  创建者：    SSC
 *  创建时间：   2018/8/4 21:08
 *  描述：     TODO用于销毁所有的Activity
 */

import android.app.Activity;

import java.util.HashSet;

public class ActivityManager {

    private static ActivityManager instance = new ActivityManager();
    private static HashSet<Activity> hashSet = new HashSet<>();
    private ActivityManager(){

    }
    public static ActivityManager getInstance() {
        return instance;
    }

    /**
     * 每一个Activity 在 onCreate 方法的时候，可以装入当前this
     * @param activity
     */
    public void addActivity(Activity activity) {
        try {
            hashSet.add(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用此方法用于销毁所有的Activity，然后我们在调用此方法之前，调到登录的Activity
     */
    public void exit() {
        try {
            for (Activity activity : hashSet) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
