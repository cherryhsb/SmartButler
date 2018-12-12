package com.ssc.smartbutler.retrofit.girl;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.retrofit.girl
 *  文件名：    GirlItem
 *  创建者：    SSC
 *  创建时间：   2018/10/17 16:29
 *  描述：     TODO
 */

import com.google.gson.annotations.SerializedName;

public class GirlItem {

    private String desc;

    @SerializedName("url")
    private String imgUrl;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "GirlItem{" +
                "desc='" + desc + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
