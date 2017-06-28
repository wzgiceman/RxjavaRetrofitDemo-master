package com.example.retrofit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.retrofit.HttpPostService;
import com.example.retrofit.R;
import com.example.retrofit.entity.api.SubjectPostApi;
import com.example.retrofit.entity.api.UploadApi;
import com.example.retrofit.entity.resulte.RetrofitEntity;
import com.example.retrofit.entity.resulte.SubjectResulte;
import com.example.retrofit.entity.resulte.UploadResulte;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseResultEntity;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.HttpManager;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpOnNextListener;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.upload.ProgressRequestBody;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.upload.UploadProgressListener;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity implements View.OnClickListener {
    private TextView tvMsg;
    private NumberProgressBar progressBar;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        findViewById(R.id.btn_simple).setOnClickListener(this);
        findViewById(R.id.btn_rx).setOnClickListener(this);
        findViewById(R.id.btn_rx_mu_down).setOnClickListener(this);
        findViewById(R.id.btn_rx_uploade).setOnClickListener(this);
        img=(ImageView)findViewById(R.id.img);
        progressBar=(NumberProgressBar)findViewById(R.id.number_progress_bar);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_simple:
                onButton9Click();
                break;
            case R.id.btn_rx:
                simpleDo();
                break;
            case R.id.btn_rx_uploade:
                uploadeDo();
                break;
            case  R.id.btn_rx_mu_down:
                Intent intent=new Intent(this,DownLaodActivity.class);
                startActivity(intent);
                break;
        }
    }

    /*************************************************封装完请求*******************************************************/

    //    完美封装简化版
    private void simpleDo() {
        SubjectPostApi postEntity = new SubjectPostApi(simpleOnNextListener,this);
        postEntity.setAll(true);
        HttpManager manager = HttpManager.getInstance();
        manager.doHttpDeal(postEntity);
    }

    //   回调一一对应
    HttpOnNextListener simpleOnNextListener = new HttpOnNextListener<List<SubjectResulte>>() {
        @Override
        public void onNext(List<SubjectResulte> subjects) {
            tvMsg.setText("网络返回：\n" + subjects.toString());
        }

        @Override
        public void onCacheNext(String cache) {
            /*缓存回调*/
            Gson gson=new Gson();
            java.lang.reflect.Type type = new TypeToken<BaseResultEntity<List<SubjectResulte>>>() {}.getType();
            BaseResultEntity resultEntity= gson.fromJson(cache, type);
            tvMsg.setText("缓存返回：\n"+resultEntity.getData().toString() );
        }

        /*用户主动调用，默认是不需要覆写该方法*/
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            tvMsg.setText("失败：\n" + e.toString());
        }

        /*用户主动调用，默认是不需要覆写该方法*/
        @Override
        public void onCancel() {
            super.onCancel();
            tvMsg.setText("取消請求");
        }
    };


    /*********************************************文件上传***************************************************/

  private void uploadeDo(){
      File file=new File("/storage/emulated/0/Download/11.jpg");
      RequestBody requestBody=RequestBody.create(MediaType.parse("image/jpeg"),file);
      MultipartBody.Part part= MultipartBody.Part.createFormData("file_name", file.getName(), new ProgressRequestBody(requestBody,
              new UploadProgressListener() {
          @Override
          public void onProgress(final long currentBytesCount, final long totalBytesCount) {

                /*回到主线程中，可通过timer等延迟或者循环避免快速刷新数据*/
              Observable.just(currentBytesCount).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {

                  @Override
                  public void call(Long aLong) {
                      tvMsg.setText("提示:上传中");
                      progressBar.setMax((int) totalBytesCount);
                      progressBar.setProgress((int) currentBytesCount);
                  }
              });
          }
      }));
      UploadApi uplaodApi = new UploadApi(httpOnNextListener,this);
      uplaodApi.setPart(part);
      HttpManager manager = HttpManager.getInstance();
      manager.doHttpDeal(uplaodApi);
  }


    /**
     * 上传回调
     */
    HttpOnNextListener httpOnNextListener=new HttpOnNextListener<UploadResulte>() {
        @Override
        public void onNext(UploadResulte o) {
            tvMsg.setText("成功");
            Glide.with(MainActivity.this).load(o.getHeadImgUrl()).skipMemoryCache(true).into(img);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            tvMsg.setText("失败："+e.toString());
        }

    };



    /**********************************************************正常不封装使用**********************************/

    /**
     * Retrofit加入rxjava实现http请求
     */
    private void onButton9Click() {
        String BASE_URL="http://www.izaodao.com/Api/";
        //手动创建一个OkHttpClient并设置超时时间
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

//        加载框
        final ProgressDialog pd = new ProgressDialog(this);

        HttpPostService apiService = retrofit.create(HttpPostService.class);
        Observable<RetrofitEntity> observable = apiService.getAllVedioBy(true);
        observable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<RetrofitEntity>() {
                            @Override
                            public void onCompleted() {
                                if (pd != null && pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (pd != null && pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }

                            @Override
                            public void onNext(RetrofitEntity retrofitEntity) {
                                tvMsg.setText("无封装：\n" + retrofitEntity.getData().toString());
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                pd.show();
                            }
                        }

                );
    }




}
