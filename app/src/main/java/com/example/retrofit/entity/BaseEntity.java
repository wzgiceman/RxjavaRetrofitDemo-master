package com.example.retrofit.entity;

import com.example.retrofit.exception.HttpTimeException;
import com.example.retrofit.http.HttpService;
import com.example.retrofit.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.functions.Func1;

/**
 * 请求数据统一封装类
 * Created by WZG on 2016/7/16.
 */
public abstract class BaseEntity<T> implements Func1<BaseResultEntity<T>, T> {
    //    rx生命周期管理
    private RxAppCompatActivity rxAppCompatActivity;
    /*回调*/
    private HttpOnNextListener listener;
    /*是否能取消加载框*/
    private boolean cancel;
    /*是否显示加载框*/
    private boolean showProgress;


    public BaseEntity(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        setListener(listener);
        setRxAppCompatActivity(rxAppCompatActivity);
        setShowProgress(true);
    }

    /**
     * 设置参数
     *
     * @param methods
     * @return
     */
    public abstract Observable getObservable(HttpService methods);

    public void setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = rxAppCompatActivity;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isCancel() {
         return cancel;
     }

     public void setCancel(boolean cancel) {
         this.cancel = cancel;
     }

     public HttpOnNextListener getListener() {
         return listener;
     }

     public void setListener(HttpOnNextListener listener) {
         this.listener = listener;
     }

    /*
     * 获取当前rx生命周期
     * @return
     */
    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity;
    }


    @Override
    public T call(BaseResultEntity<T> httpResult) {
        if (httpResult.getRet() == 0) {
            throw new HttpTimeException(httpResult.getMsg());
        }
        return httpResult.getData();
    }
}
