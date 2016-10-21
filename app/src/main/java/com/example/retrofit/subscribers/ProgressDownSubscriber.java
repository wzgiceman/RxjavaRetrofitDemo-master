package com.example.retrofit.subscribers;


import com.example.retrofit.entity.DownInfo;
import com.example.retrofit.http.HttpDownManager;
import com.example.retrofit.listener.DownLoadListener.DownloadProgressListener;
import com.example.retrofit.listener.HttpProgressOnNextListener;

import java.lang.ref.WeakReference;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by WZG on 2016/7/16.
 */
public class ProgressDownSubscriber<T> extends Subscriber<T> implements DownloadProgressListener {
    //    弱引用结果回调
    private WeakReference<HttpProgressOnNextListener> mSubscriberOnNextListener;
    /*数据*/
    private DownInfo baseDownEntity;


    public ProgressDownSubscriber(DownInfo baseDownEntity) {
        this.mSubscriberOnNextListener = new WeakReference<>(baseDownEntity.getListener());
        setBaseDownEntity(baseDownEntity);
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        if(mSubscriberOnNextListener.get()!=null){
            mSubscriberOnNextListener.get().onStart();
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        if(mSubscriberOnNextListener.get()!=null){
            mSubscriberOnNextListener.get().onComplete();
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if(mSubscriberOnNextListener.get()!=null){
            mSubscriberOnNextListener.get().onError(e);
        }
        /*停止下载*/
        HttpDownManager.getInstance().stopDown(baseDownEntity);
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener.get() != null) {
            mSubscriberOnNextListener.get().onNext(t);
        }
    }

    @Override
    public void update(long read, long count, boolean done) {
        if(baseDownEntity.getCountLength()>count){
            read=baseDownEntity.getCountLength()-count+read;
        }else{
            baseDownEntity.setCountLength(count);
        }
        baseDownEntity.setReadLength(read);
        if (mSubscriberOnNextListener.get() != null) {
            rx.Observable.just(read).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    mSubscriberOnNextListener.get().updateProgress(aLong,baseDownEntity.getCountLength());
                }
            });
        }
    }

    public DownInfo getBaseDownEntity() {
        return baseDownEntity;
    }

    public void setBaseDownEntity(DownInfo baseDownEntity) {
        this.baseDownEntity = baseDownEntity;
    }

    public WeakReference<HttpProgressOnNextListener> getmSubscriberOnNextListener() {
        return mSubscriberOnNextListener;
    }

    public void setmSubscriberOnNextListener(WeakReference<HttpProgressOnNextListener> mSubscriberOnNextListener) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
    }
}