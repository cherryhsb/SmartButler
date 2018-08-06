package com.ssc.smartbutler.entity;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.entity
 *  文件名：    WechatData
 *  创建者：    SSC
 *  创建时间：   2018/8/3 18:12
 *  描述：     TODO:
 */

public class WechatData {

    //标题
    private String title;
    //出处
    private String source;
    //图片url
    private String imgUrl;
    //新闻url
    private String newUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    @Override
    public String toString() {
        return "WechatData{" +
                "title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", newUrl='" + newUrl + '\'' +
                '}';
    }
}
