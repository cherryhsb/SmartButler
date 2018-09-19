package com.ssc.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.view.DispatchLinearLayout;

public class SmsService extends Service {

    private SmsReceiver smsReceiver;

    private static final String TAG = "SmsService";

    //号码,短信内容
    private String smsPhone, smsContent;

    private Button btn_sms;

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
        L.i(TAG, "onCheckedChanged: 启动了服务");
        init();
    }

    private void init() {
        L.i(TAG, "init");
        //动态注册短信广播
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加Action
        //WifiManager.SUPPLICANT_STATE_CHANGED_ACTION
        //WifiManager.WIFI_STATE_CHANGED_ACTION
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        //设置权限
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver, intentFilter);

        //动态注册Home监听广播
        homeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intentFilter1 = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatchReceiver, intentFilter1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i(TAG, "stop");
        //取消注册
        unregisterReceiver(smsReceiver);

        unregisterReceiver(homeWatchReceiver);
    }

    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //L.i(TAG,"来短信了");
            String action = intent.getAction();
            if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(action)) {
                L.i(TAG, "来短信了");
                //获取短信内容返回的是一个Object数组
                /*Object[] pdus = (Object[]) intent.getExtras().get("pdus");
                //遍历数组,得到相关数据
                for (Object obj:pdus){
                    //把数组元素转换成短信对象
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                    smsPhone = smsMessage.getOriginatingAddress();
                    smsContent = smsMessage.getMessageBody();
                    L.i(TAG,smsPhone+smsContent);
                }*/
                showWindow();
            }
        }
    }

    //窗口管理器
    private WindowManager manager;
    //布局参数
    private WindowManager.LayoutParams layoutParams;
    //窗口提示
    //View
    private DispatchLinearLayout mView;

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
        mView = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.item_sms, null);

        btn_sms=mView.findViewById(R.id.btn_sms);
        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
                //消失窗口
                if (mView.getParent() != null){
                    L.i(TAG,mView.getParent().toString());
                    manager.removeView(mView);
                }
            }
        });

        //添加View到窗口
        //需要开启悬浮窗权限
        manager.addView(mView, layoutParams);

        mView.setDispatchKeyEventListener(mDispatchKeyEventListener);

    }

    private DispatchLinearLayout.DispatchKeyEventListener mDispatchKeyEventListener
            = new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否是按返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                L.i(TAG,"我按了BACK键");
                if (mView.getParent() != null){
                    L.i(TAG,"我按了BACK键2");
                    //manager.removeView(mView);
                    manager.removeViewImmediate(mView);
                }
                //manager.removeView(mView);
                return true;
            }
            return false;
        }
    };

    //回复短信
    private void sendSms() {
        Uri uri = Uri.parse("smsto:" + "10010");
        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body","");
        startActivity(intent);
    }


    public static final String SYSTEM_DIALOGS_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY = "homekey";
    HomeWatchReceiver homeWatchReceiver;
    //监听Home键的广播
    class HomeWatchReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason = intent.getStringExtra(SYSTEM_DIALOGS_REASON_KEY);
                if (SYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    L.i(TAG,"我按了HOME键");
                    if (mView.getParent() != null){
                        L.i(TAG,"我按了HOME键2");
                        //manager.removeView(mView);
                        manager.removeViewImmediate(mView);
                    }
                }
            }
        }
    }
}
