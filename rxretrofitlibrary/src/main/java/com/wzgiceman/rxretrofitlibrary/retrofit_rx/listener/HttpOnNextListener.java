package com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener;

/**
 * 成功回调处理
 * Created by WZG on 2016/7/16.
 */
public abstract class HttpOnNextListener<T> {
    /**
     * 成功后回调方法
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 緩存回調結果
     * @param string
     */
    public void onCacheNext(String string){

    }

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     * @param e
     */
    public  void onError(Throwable e){

    }

    /**
     * 取消回調
     */
    public void onCancel(){

    }
}
