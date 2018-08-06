package com.ssc.smartbutler.entity;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.entity
 *  文件名：    ChatData
 *  创建者：    SSC
 *  创建时间：   2018/7/31 9:30
 *  描述：     对话列表的实体
 */

public class ChatData {

    private String text;

    //区分左右
    private int type;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ChatData{" +
                "text='" + text + '\'' +
                ", type=" + type +
                '}';
    }
}
