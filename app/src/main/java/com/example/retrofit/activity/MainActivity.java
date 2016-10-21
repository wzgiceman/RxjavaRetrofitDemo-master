package com.example.retrofit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.retrofit.R;
import com.example.retrofit.entity.DownInfo;
import com.example.retrofit.entity.RetrofitEntity;
import com.example.retrofit.entity.Subject;
import com.example.retrofit.entity.SubjectPostApi;
import com.example.retrofit.entity.UplaodApi;
import com.example.retrofit.entity.UploadResulte;
import com.example.retrofit.http.HttpDownManager;
import com.example.retrofit.http.HttpManager;
import com.example.retrofit.http.HttpService;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.listener.HttpProgressOnNextListener;
import com.example.retrofit.listener.upload.ProgressRequestBody;
import com.example.retrofit.listener.upload.UploadProgressListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

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
        findViewById(R.id.btn_rx_down).setOnClickListener(this);
        findViewById(R.id.btn_rx_uploade).setOnClickListener(this);
        findViewById(R.id.btn_rx_mu_down).setOnClickListener(this);
        findViewById(R.id.btn_rx_pause).setOnClickListener(this);
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
            case R.id.btn_rx_down:
                downApk();
                break;
            case R.id.btn_rx_uploade:
                uploadeDo();
                break;
            case R.id.btn_rx_pause:
                pause();
                break;
            case  R.id.btn_rx_mu_down:
                Intent intent=new Intent(this,DownLaodActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * Retrofit加入rxjava实现http请求
     */
    private void onButton9Click() {
        //手动创建一个OkHttpClient并设置超时时间
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(HttpManager.BASE_URL)
                .build();

//        加载框
        final ProgressDialog pd = new ProgressDialog(this);

        HttpService apiService = retrofit.create(HttpService.class);
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

    /*************************************************一般请求*******************************************************/


    //    完美封装简化版
    private void simpleDo() {
        SubjectPostApi postEntity = new SubjectPostApi(simpleOnNextListener,this);
        postEntity.setAll(true);
        HttpManager manager = HttpManager.getInstance();
        manager.doHttpDeal(postEntity);
    }

    //   回调一一对应
    HttpOnNextListener simpleOnNextListener = new HttpOnNextListener<List<Subject>>() {
        @Override
        public void onNext(List<Subject> subjects) {
            tvMsg.setText("已封装：\n" + subjects.toString());
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


    /******************************************* 下載 **********************************************/
    DownInfo apkApi;
    /*下载处理 6.0以后的手机需要加入权限判断*/
    private void downApk(){
        HttpDownManager manager=HttpDownManager.getInstance();
        if(apkApi==null){
//            String apkUrl="http://download.fir.im/v2/app/install/572eec6fe75e2d7a05000008?download_token=572bcb03dad2eed7c758670fd23b5ac4";
            String apkUrl="http://www.izaodao.com/app/izaodao_app.apk";
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "test3" +
                    ".apk");
            apkApi=new DownInfo(apkUrl,httpProgressOnNextListener);
            apkApi.setSavePath(outputFile.getAbsolutePath());
        }
        manager.startDown(apkApi);
    }

    /*暂停下载*/
    private void pause(){
        if(apkApi!=null){
            HttpDownManager.getInstance().pause(apkApi);
        }
    }

    /*下载回调*/
    HttpProgressOnNextListener<DownInfo> httpProgressOnNextListener=new HttpProgressOnNextListener<DownInfo>() {
        @Override
        public void onNext(DownInfo baseDownEntity) {
            Toast.makeText(MainActivity.this,baseDownEntity.getSavePath(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStart() {
            tvMsg.setText("提示:开始下载");
        }

        @Override
        public void onComplete() {
            tvMsg.setText("提示：下载完成");
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            tvMsg.setText("失败:"+e.toString());
        }


        @Override
        public void onPuase() {
            super.onPuase();
            tvMsg.setText("提示:暂停");
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void updateProgress(long readLength, long countLength) {
            tvMsg.setText("提示:下载中");
            progressBar.setMax((int) countLength);
            progressBar.setProgress((int) readLength);
        }
    };


    /*********************************************文件上传***************************************************/

  private void uploadeDo(){
      File file=new File("/storage/emulated/0/Download/11.jpg");
      RequestBody requestBody=RequestBody.create(MediaType.parse("image/jpeg"),file);
      MultipartBody.Part part= MultipartBody.Part.createFormData("file_name", file.getName(), new ProgressRequestBody(requestBody,
              new UploadProgressListener() {
          @Override
          public void onProgress(long currentBytesCount, long totalBytesCount) {
              tvMsg.setText("提示:上传中");
              progressBar.setMax((int) totalBytesCount);
              progressBar.setProgress((int) currentBytesCount);
          }
      }));
      UplaodApi uplaodApi = new UplaodApi(httpOnNextListener,this);
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
}
