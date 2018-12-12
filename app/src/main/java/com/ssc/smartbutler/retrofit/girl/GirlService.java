package com.ssc.smartbutler.retrofit.girl;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.retrofit.girl
 *  文件名：    GirlService
 *  创建者：    SSC
 *  创建时间：   2018/10/17 16:20
 *  描述：     TODO
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GirlService {

    @GET("/api/data/%E7%A6%8F%E5%88%A9/20/{page}")
    Call<GirlPicture> getGirl(@Path("page") String page);
}
