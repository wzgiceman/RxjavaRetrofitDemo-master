package com.example.retrofit.entity;

import com.example.retrofit.exception.HttpTimeException;
import com.example.retrofit.http.HttpService;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.subscribers.ProgressSubscriber;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public abstract class BaseEntity<T> implements Func1<BaseResultEntity<T>, T> {
    //    rx生命周期管理
    private RxAppCompatActivity rxAppCompatActivity;
    /*sub预处理类*/
    protected ProgressSubscriber progressSubscriber;


    public BaseEntity(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        progressSubscriber=new ProgressSubscriber(listener,rxAppCompatActivity);
        this.rxAppCompatActivity=rxAppCompatActivity;
    }

    public BaseEntity(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity,boolean cancel) {
        progressSubscriber=new ProgressSubscriber(listener,rxAppCompatActivity,cancel);
        this.rxAppCompatActivity=rxAppCompatActivity;
    }


    /**
     * 获取当前rx生命周期
     * @return
     */
    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity;
    }

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
    public Subscriber getSubscirber(){
        return  progressSubscriber;
    }


    @Override
    public T call(BaseResultEntity<T> httpResult) {
        if (httpResult.getRet() == 0) {
            throw new HttpTimeException(httpResult.getMsg());
        }
        return httpResult.getData();
    }
}
