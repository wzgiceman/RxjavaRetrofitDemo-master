package com.example.retrofit.entity;

import com.example.retrofit.http.HttpService;
import com.example.retrofit.listener.HttpProgressOnNextListener;

import rx.Observable;

/**
 * apk下载请求类
 * Created by WZG on 2016/10/20.
 */

public class DownApkApi extends BaseDownEntity{

    public DownApkApi(String url,HttpProgressOnNextListener listener) {
        super(url,listener);
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.downloadApk(getUrl());
    }
}
