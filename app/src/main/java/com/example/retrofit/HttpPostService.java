package com.example.retrofit;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseResultEntity;
import com.example.retrofit.entity.resulte.RetrofitEntity;
import com.example.retrofit.entity.resulte.SubjectResulte;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
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

    @POST("AppFiftyToneGraph/videoLink")
    Observable<BaseResultEntity<List<SubjectResulte>>> getAllVedioBys(@Body boolean once_no);

}
