package com.ssc.smartbutler.download;

/*
 *  项目名：    helloworld
 *  包名:       com.ssc.servicebestpractice
 *  文件名：    DownloadListener
 *  创建者：    SSC
 *  创建时间：   2018/9/25 13:17
 *  描述：     回调接口
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
