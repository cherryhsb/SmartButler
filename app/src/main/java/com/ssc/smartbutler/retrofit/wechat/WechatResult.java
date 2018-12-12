package com.ssc.smartbutler.retrofit.wechat;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter.retrofit
 *  文件名：    WechatResult
 *  创建者：    SSC
 *  创建时间：   2018/10/16 17:19
 *  描述：     TODO
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WechatResult {

    private int ps;

    private int pno;

    @SerializedName("list")
    private List<WechatItem> wechatItemList;

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPno() {
        return pno;
    }

    public void setPno(int pno) {
        this.pno = pno;
    }

    public List<WechatItem> getWechatItemList() {
        return wechatItemList;
    }

    public void setWechatItemList(List<WechatItem> wechatItemList) {
        this.wechatItemList = wechatItemList;
    }
}
