package com.example.retrofit;

import com.example.retrofit.entity.resulte.RetrofitEntity;
import com.example.retrofit.entity.resulte.SubjectResulte;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseResultEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 测试接口service-post相关
 * Created by WZG on 2016/12/19.
 */

public interface HttpPostService {
    @POST("AppFiftyToneGraph/videoLink")
    Call<RetrofitEntity> getAllVedio(@Body boolean once_no);

    @POST("AppFiftyToneGraph/videoLink")
    Observable<RetrofitEntity> getAllVedioBy(@Body boolean once_no);

//    @POST("AppFiftyToneGraph/videoLink")
//    Observable<BaseResultEntity<List<SubjectResulte>>> getAllVedioBys(@Body boolean once_no);

    @GET("AppFiftyToneGraph/videoLink/{once_no}")
    Observable<BaseResultEntity<List<SubjectResulte>>> getAllVedioBys(@Query("once_no") boolean once_no);

}
