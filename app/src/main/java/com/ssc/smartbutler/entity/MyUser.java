package com.ssc.smartbutler.entity;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.entity
 *  创建者：    SSC
 *  创建时间：   2018/7/12 1:51
 *  描述：     用户属性
 */

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser{

    private String nickname;

    private int age;

    private boolean gender;

    private String desc;

    private String imgString;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImgString() {
        return imgString;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
