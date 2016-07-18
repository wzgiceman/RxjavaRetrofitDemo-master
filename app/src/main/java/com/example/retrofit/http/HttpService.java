package com.example.retrofit.http;

import com.example.retrofit.entity.BaseResultEntity;
import com.example.retrofit.entity.RetrofitEntity;
import com.example.retrofit.entity.Subject;

import java.util.List;

import retrofit.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * service统一接口数据
 * Created by WZG on 2016/7/16.
 */
public interface HttpService {

    @retrofit.http.POST("AppFiftyToneGraph/videoLink")
    Call<RetrofitEntity> getAllVedio(@retrofit.http.Body boolean once_no);

    @POST("AppFiftyToneGraph/videoLink")
    Observable<RetrofitEntity> getAllVedioBy(@Body boolean once_no);

    @POST("AppFiftyToneGraph/videoLink")
    Observable<BaseResultEntity<List<Subject>>> getAllVedioBys(@Body boolean once_no);

}
