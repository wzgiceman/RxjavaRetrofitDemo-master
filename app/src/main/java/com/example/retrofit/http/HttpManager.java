package com.example.retrofit.http;

import com.example.retrofit.entity.BaseEntity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * http交互处理类
 * Created by WZG on 2016/7/16.
 */
public class HttpManager {
    public static final String BASE_URL = "http://www.izaodao.com/Api/";
    private static final int DEFAULT_TIMEOUT = 6;
    private volatile static HttpManager INSTANCE;
    private HttpService httpService;

    //构造方法私有
    private HttpManager() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    //获取单例
    public static HttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(BaseEntity basePar) {
        Observable observable = basePar.getObservable(httpService);
//        统一结果判断处理
        observable.map(basePar);
//        http请求后台线程中执行
        observable.subscribeOn(Schedulers.io());
//        取消后及时取消后台运的线程
        observable.unsubscribeOn(Schedulers.io());
//        回调在主线程中执行
        observable.observeOn(AndroidSchedulers.mainThread());
//        回调subscribe
        observable.subscribe(basePar.getSubscirber());
    }
}
