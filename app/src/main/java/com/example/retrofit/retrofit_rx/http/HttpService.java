package com.example.retrofit.retrofit_rx.http;

import com.example.retrofit.retrofit_rx.Api.BaseResultEntity;
import com.example.retrofit.entity.resulte.RetrofitEntity;
import com.example.retrofit.entity.resulte.SubjectResulte;
import com.example.retrofit.entity.resulte.UploadResulte;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
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
    Observable<BaseResultEntity<List<SubjectResulte>>> getAllVedioBys(@Body boolean once_no);

    /*断点续传下载接口*/
    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);

    /*上传文件*/
    @Multipart
    @POST("AppYuFaKu/uploadHeadImg")
    Observable<BaseResultEntity<UploadResulte>> uploadImage(@Part("uid") RequestBody uid, @Part("auth_key") RequestBody  auth_key,
                                                            @Part MultipartBody.Part file);

}
