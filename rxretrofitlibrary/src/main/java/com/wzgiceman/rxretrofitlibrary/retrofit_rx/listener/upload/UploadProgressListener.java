package com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.upload;

/**
 * 上传进度回调类
 * Created by WZG on 2016/10/20.
 */

public interface UploadProgressListener {
    /**
     * 上传进度
     * @param currentBytesCount
     * @param totalBytesCount
     */
    void onProgress(long currentBytesCount, long totalBytesCount);
}