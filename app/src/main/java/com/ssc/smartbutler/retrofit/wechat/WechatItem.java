package com.ssc.smartbutler.retrofit.wechat;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter.retrofit
 *  文件名：    WechatItem
 *  创建者：    SSC
 *  创建时间：   2018/10/16 17:21
 *  描述：     TODO
 */

public class WechatItem {

    private String title;

    private String source;

    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "WechatItem{" +
                "title='" + title + '\'' +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
