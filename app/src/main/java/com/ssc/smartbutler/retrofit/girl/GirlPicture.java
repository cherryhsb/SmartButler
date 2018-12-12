package com.ssc.smartbutler.retrofit.girl;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.retrofit.girl
 *  文件名：    GirlPicture
 *  创建者：    SSC
 *  创建时间：   2018/10/17 16:33
 *  描述：     TODO
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GirlPicture {

    private boolean error;

    @SerializedName("results")
    private List<GirlItem> girlItemList;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<GirlItem> getGirlItemList() {
        return girlItemList;
    }

    public void setGirlItemList(List<GirlItem> girlItemList) {
        this.girlItemList = girlItemList;
    }
}
