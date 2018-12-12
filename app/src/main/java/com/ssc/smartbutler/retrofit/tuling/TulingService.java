package com.ssc.smartbutler.retrofit.tuling;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.retrofit.tuling
 *  文件名：    TulingService
 *  创建者：    SSC
 *  创建时间：   2018/10/17 15:04
 *  描述：     retrofit 图灵机器人
 */

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TulingService {

    @POST("/openapi/api")
    @FormUrlEncoded
    Call<Tuling> getTuling(@Field("key") String key, @Field("info") String info, @Field("userid") String userid);
}
