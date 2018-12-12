package com.ssc.smartbutler.retrofit.tuling;

/*
 *  项目名：    helloworld
 *  包名:       com.ssc.retrofitdemo.chat
 *  文件名：    Tuling
 *  创建者：    SSC
 *  创建时间：   2018/10/17 13:35
 *  描述：     TODO
 */

public class Tuling {

    private int code;

    private String text;

    private String url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Tuling{" +
                "code=" + code +
                ", text='" + text + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
