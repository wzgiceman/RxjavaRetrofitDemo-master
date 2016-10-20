package com.example.retrofit.http;

import com.example.retrofit.entity.BaseDownEntity;
import com.example.retrofit.exception.HttpTimeException;
import com.example.retrofit.exception.RetryWhenNetworkException;
import com.example.retrofit.listener.DownLoadListener.DownloadInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * http下载处理类
 * Created by WZG on 2016/7/16.
 */
public class HttpDownManager {
    /*超时设置*/
    private static final int DEFAULT_TIMEOUT = 2;

    /**
     * 处理下载请求
     *
     * @param basePar 封装的请求数据
     */
    public void downDeal(BaseDownEntity basePar) {
        DownloadInterceptor interceptor = new DownloadInterceptor(basePar.getSubscriber());
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //手动创建一个OkHttpClient并设置超时时间
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();
        HttpService httpService = retrofit.create(HttpService.class);


        /*得到rx对象*/
        Observable observable = basePar.getObservable(httpService)
                /*指定线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*读取下载写入文件*/
                .map(new Func1<ResponseBody, BaseDownEntity>() {
                    @Override
                    public BaseDownEntity call(ResponseBody responseBody) {
                        try {
                            writeFile(responseBody.byteStream(),new File(basePar.getSavePath()));
                        } catch (IOException e) {
                            /*失败抛出异常*/
                            throw new HttpTimeException(e.getMessage());
                        }
                        return basePar;
                    }
                })
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread());
        /*数据回调*/
        observable.subscribe(basePar.getSubscriber());
    }


    /**
     * 写入文件
     *
     * @param in
     * @param file
     */
    public  void writeFile(InputStream in, File file) throws IOException {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (file != null && file.exists())
            file.delete();
        FileOutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024 * 128];
        int len = -1;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();
    }

}
