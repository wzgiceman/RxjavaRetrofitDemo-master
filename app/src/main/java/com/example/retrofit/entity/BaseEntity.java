package com.example.retrofit.entity;

import com.example.retrofit.http.HttpService;
import com.example.retrofit.exception.HttpTimeException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public abstract class BaseEntity<T> implements Func1<BaseResultEntity<T>, T> {
    /**
     * 设置参数
     *
     * @param methods
     * @return
     */
    public abstract Observable getObservable(HttpService methods);

    /**
     * 设置回调sub
     *
     * @return
     */
    public abstract Subscriber getSubscirber();


    @Override
    public T call(BaseResultEntity<T> httpResult) {
        if (httpResult.getRet() == 0) {
            throw new HttpTimeException(httpResult.getMsg());
        }
        return httpResult.getData();
    }
}
