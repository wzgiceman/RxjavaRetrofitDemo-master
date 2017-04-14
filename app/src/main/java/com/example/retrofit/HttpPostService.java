package com.example.retrofit;

import com.example.retrofit.entity.resulte.RetrofitEntity;
import com.example.retrofit.entity.resulte.SubjectResulte;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseResultEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @FormUrlEncoded
    @POST("AppFiftyToneGraph/videoLink")
    Observable<BaseResultEntity<List<SubjectResulte>>> getAllVedioBys(@Field("once") boolean once_no);

}
