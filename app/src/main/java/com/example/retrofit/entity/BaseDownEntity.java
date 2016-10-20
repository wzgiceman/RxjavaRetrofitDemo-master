package com.example.retrofit.entity;

import com.example.retrofit.listener.HttpProgressOnNextListener;
import com.example.retrofit.http.HttpService;
import com.example.retrofit.subscribers.ProgressDownSubscriber;

import rx.Observable;

/**
 * 下载请求数据基础类
 * Created by WZG on 2016/10/20.
 */

public abstract class  BaseDownEntity {
    /*存储位置*/
    private String savePath;
    /*下载url*/
    private String url;
    /*基础url*/
    private String baseUrl;
    /*下载回调sub*/
    private ProgressDownSubscriber subscriber;

    public BaseDownEntity(String url,HttpProgressOnNextListener listener) {
        setUrl(url);
        setBaseUrl(getBasUrl(url));
        subscriber=new ProgressDownSubscriber(listener);
    }

    /**
     * 设置参数
     *
     * @param methods
     * @return
     */
    public abstract Observable getObservable(HttpService methods);

    public ProgressDownSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(ProgressDownSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 读取baseurl
     * @param url
     * @return
     */
    protected String getBasUrl(String url) {
        String head = "";
        int index = url.indexOf("://");
        if (index != -1) {
            head = url.substring(0, index + 3);
            url = url.substring(index + 3);
        }
        index = url.indexOf("/");
        if (index != -1) {
            url = url.substring(0, index + 1);
        }
        return head + url;
    }
}
