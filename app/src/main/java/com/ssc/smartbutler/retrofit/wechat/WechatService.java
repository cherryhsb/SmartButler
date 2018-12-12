package com.ssc.smartbutler.retrofit.wechat;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter.retrofit
 *  文件名：    WechatService
 *  创建者：    SSC
 *  创建时间：   2018/10/16 17:16
 *  描述：     retrofit 微信精选
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WechatService {

    @GET("/weixin/query?key=948e7d95f9edb7494b1bb253031d7461")
    Call<WechatNews> getWechatNews(@Query("pno") int pno, @Query("ps")int ps);

    /*pno	否	int	当前页数，默认1
    ps	否	int	每页返回条数，最大50，默认20
    key	是	string	在个人中心->我的数据,接口名称上方查看
    dtype	否	string	返回数据的格式,xml或json，默认json*/
}
