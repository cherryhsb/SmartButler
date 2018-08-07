package com.ssc.smartbutler.entity;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.entity
 *  文件名：    GirlData
 *  创建者：    SSC
 *  创建时间：   2018/8/6 14:04
 *  描述：     TODO美女数据
 */

public class GirlData {

    private String imgUrl;

    private String desc;

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
        return "GirlData{" +
                "imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
