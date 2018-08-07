package com.ssc.smartbutler.utils;
/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.utils
 *  创建者：    SSC
 *  创建时间：   2018/7/7 6:49
 *  描述：     数据/常量
 */

import com.ssc.smartbutler.entity.MyUser;

public class StaticClass {

    //闪屏页延时
    public static final int HANDLER_SPLASH = 1001;
    //没有授权,退出
    public static final int HANDLER_EXIT = 1002;
    //判断程序是否是第一次运行
    public static final String SHARE_IS_FIRST = "isFirst";
    //判断程序是否提示:归属地
    public static final String SHARE_IS_HINT_LOCATION = "isHintLocation";
    //Bugly
    public static final String BUGLY_ID = "96d5d235e6";
    //Bmob
    public static final String BMOB_ID = "d6b8dd02d1957ce5f580805ff0075bf9";
    //快递
    public static final String EXPRESS_ID = "f1a24917ef158c9d6da188cf55dc1b67";
    //归属地
    public static final String LOCATION_ID = "cd4acdb8dd9fb16172a0e36084a8e33d";
    //微信精选
    public static final String WECHAT_ID = "948e7d95f9edb7494b1bb253031d7461";
    //机器人
    public static final String TULING_ID = "068ec76b99524df7881349deb27f660a";
    //讯飞
    //public static final String XUNFEI_ID = "5b690f77";
    //美女相册
    public static final String GIRL_URL = "https://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/1";
    //退出返回MainActivity
    public static final int REQUEST_CODE_EXIT = 10001;
    //登录
    public static final int REQUEST_CODE_LOGIN = 10002;
    //注册
    public static final int REQUEST_CODE_REGISTER = 10003;
}
