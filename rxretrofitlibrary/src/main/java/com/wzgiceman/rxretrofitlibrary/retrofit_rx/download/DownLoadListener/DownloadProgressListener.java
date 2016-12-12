package com.wzgiceman.rxretrofitlibrary.retrofit_rx.download.DownLoadListener;


/**
 * 成功回调处理
 * Created by WZG on 2016/10/20.
 */
public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
