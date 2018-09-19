package com.ssc.smartbutler.utils;
/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.utils
 *  创建者：    SSC
 *  创建时间：   2018/7/7 6:49
 *  描述：     数据/常量
 */

public class StaticClass {

    //判断程序是否是第一次运行
    public static final String SHARE_IS_FIRST = "isFirst";
    //判断是否开启语音机器人
    public static final String IS_TTS = "isTTS";
    //判断是否开启短信提醒
    public static final String IS_SMS = "isSMS";
    //判断程序是否提示:归属地
    public static final String SHARE_IS_HINT_LOCATION = "isHintLocation";
    //判断小米手机:短信权限
    public static final String SHARE_KNOW_MIUI_SMS = "isMiuiSms";

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
    public static final String TTS_ID = "5b690f77";
    //美女相册
    public static final String GIRL_URL = "https://gank.io/api/data/%E7%A6%8F%E5%88%A9/20/1";


    //startActivityForResult:个人信息requestCode
    public static final int DESC_REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int PICTURE_REQUEST_CODE = 102;
    public static final int ZOOM_REQUEST_CODE = 103;
    //扫描二维码requestCode
    public static final int SCAN_REQUEST_CODE = 104;
    //相册扫描二维码
    public static final int IMAGE_REQUEST_CODE = 105;
    //退出返回MainActivity
    public static final int REQUEST_CODE_EXIT = 106;
    //登录
    public static final int REQUEST_CODE_LOGIN = 107;


    //短信Action
    public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    //wifiAction
    public static final String WIFI_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    //public static final String WIFI_ACTION = "WifiManager.WIFI_STATE_CHANGED_ACTION";
    //蓝牙Action
    public static final String BT_ACTION = "BluetoothAdapter.ACTION_STATE_CHANGED";
    //版本更新
    public static final String CHECK_UPDATE_URL = "http://192.168.0.108:8080/ssc/config.json";

    //权限相关常量
    public static final int INIT_PERMISSION_CODE = 1001;
    public static final int CAMERA_CODE = 1002;
    public static final int SMS_CODE = 1003;


}
