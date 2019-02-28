package com.ssc.smartbutler.utils;

/*
 *  项目名：    helloworld
 *  包名:       com.ssc.materialdesign.Utils
 *  文件名：    GetWindowManager
 *  创建者：    SSC
 *  创建时间：   2019/2/3 16:50
 *  描述：     TODO
 */

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.ssc.smartbutler.application.BaseApplication;

public class GetWindowManager {

    public static void getAndroidScreenProperty() {
        WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        Log.d("h_bl", "屏幕宽度（像素）：" + width);
        Log.d("h_bl", "屏幕高度（像素）：" + height);
        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
    }

    public static int getScreenWidth(){
        WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        return screenWidth;
    }

    public static float getScreenDensity() {
        WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        return density;
    }
}
