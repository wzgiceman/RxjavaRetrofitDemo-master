package com.example.retrofit.entity;

import com.example.retrofit.http.HttpService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.Subscriber;

/**
 * 测试数据
 * Created by WZG on 2016/7/16.
 */
public class SubjectPost extends BaseEntity {
    //    回调sub
    private Subscriber mSubscriber;
//    接口需要传入的参数 可自定义不同类型
    private boolean all;


    public SubjectPost(Subscriber getTopMovieOnNext, boolean all, RxAppCompatActivity rxAppCompatActivity) {
        this.mSubscriber = getTopMovieOnNext;
        this.all = all;
        setRxAppCompatActivity(rxAppCompatActivity);
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
