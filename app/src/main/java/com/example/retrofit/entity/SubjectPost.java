package com.example.retrofit.entity;

import com.example.retrofit.http.HttpService;

import rx.Observable;
import rx.Subscriber;

/**
 * 测试数据
 * Created by WZG on 2016/7/16.
 */
public class SubjectPost extends BaseEntity {
    //    回调sub
    private Subscriber mSubscriber;
    private boolean all;


    public SubjectPost(Subscriber getTopMovieOnNext, boolean all) {
        this.mSubscriber = getTopMovieOnNext;
        this.all = all;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getAllVedioBys(all);
    }

    @Override
    public Subscriber getSubscirber() {
        return mSubscriber;
    }

}
