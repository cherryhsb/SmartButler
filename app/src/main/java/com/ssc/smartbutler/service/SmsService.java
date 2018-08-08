package com.ssc.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.WindowManager;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.L;

import static com.ssc.smartbutler.utils.StaticClass.SMS_ACTION;

public class SmsService extends Service {

    private SmsReceiver smsReceiver;

    private static final String TAG = "SmsService";

    //号码,短信内容
    private String smsPhone,smsContent;

    public SmsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        L.i(TAG,"init");
        //动态注册广播
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加Action
        intentFilter.addAction(SMS_ACTION);
        //设置权限
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i(TAG,"stop");
        //取消注册
        unregisterReceiver(smsReceiver);
    }

    public class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //L.i(TAG,"来短信了");
            String action = intent.getAction();
            if (SMS_ACTION.equals(action)){
                L.i(TAG,"来短信了");
                //获取短信内容返回的是一个Object数组
                Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                //遍历数组,得到相关数据
                for (Object obj:pdus){
                    //把数组元素转换成短信对象
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                    smsPhone = smsMessage.getOriginatingAddress();
                    smsContent = smsMessage.getMessageBody();
                    L.i(TAG,smsPhone+smsContent);
                    showWindow();
                }
            }
        }
    }

    //窗口管理器
    private WindowManager manager;
    //布局参数
    private WindowManager.LayoutParams layoutParams;
    //窗口提示
    //View
    private View mView;
    private void showWindow() {
        //获取系统服务
        manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutParams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //加载布局
        mView = View.inflate(getApplicationContext(), R.layout.item_sms,null);

        //添加View到窗口
        //manager.addView(mView,layoutParams);
    }
}
