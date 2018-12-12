package com.ssc.smartbutler.retrofit.wechat;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter.retrofit
 *  文件名：    WechatNews
 *  创建者：    SSC
 *  创建时间：   2018/10/16 17:18
 *  描述：     TODO
 */

import com.google.gson.annotations.SerializedName;

public class WechatNews {

    private String reason;

    private int error_code;

    @SerializedName("result")
    private WechatResult wechatResult;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public WechatResult getWechatResult() {
        return wechatResult;
    }

    public void setWechatResult(WechatResult wechatResult) {
        this.wechatResult = wechatResult;
    }
}
