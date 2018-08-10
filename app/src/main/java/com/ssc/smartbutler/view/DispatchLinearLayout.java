package com.ssc.smartbutler.view;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.view
 *  文件名：    DispatchLinearLayout
 *  创建者：    SSC
 *  创建时间：   2018/8/9 18:47
 *  描述：     TODO事件分发
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class DispatchLinearLayout extends LinearLayout{

    private DispatchKeyEventListener dispatchKeyEventListener;

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return dispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    public DispatchLinearLayout(Context context) {
        super(context);
    }

    public DispatchLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //接口
    public static interface DispatchKeyEventListener{
        boolean dispatchKeyEvent(KeyEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果不为空,说明调用了,去获取事件
        if (dispatchKeyEventListener != null){
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
