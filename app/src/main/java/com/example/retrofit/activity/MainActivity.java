package com.example.retrofit.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.retrofit.R;
import com.example.retrofit.entity.BaseDownEntity;
import com.example.retrofit.entity.DownApkApi;
import com.example.retrofit.entity.RetrofitEntity;
import com.example.retrofit.entity.Subject;
import com.example.retrofit.entity.SubjectPostApi;
import com.example.retrofit.http.HttpDownManager;
import com.example.retrofit.http.HttpManager;
import com.example.retrofit.http.HttpService;
import com.example.retrofit.listener.HttpOnNextListener;
import com.example.retrofit.listener.HttpProgressOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        findViewById(R.id.btn_simple).setOnClickListener(this);
        findViewById(R.id.btn_rx).setOnClickListener(this);
        findViewById(R.id.btn_rx_down).setOnClickListener(this);
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

    /*下载处理 6.0以后的手机需要加入权限判断*/
    private void downApk(){
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.apk");
        String apkUrl="http://www.izaodao.com/app/izaodao_app.apk";
        DownApkApi apkApi=new DownApkApi(apkUrl,httpProgressOnNextListener);
        apkApi.setSavePath(outputFile.getAbsolutePath());
        HttpDownManager manager=new HttpDownManager();
        manager.downDeal(apkApi);
    }


    /*下载回调*/
    HttpProgressOnNextListener<BaseDownEntity> httpProgressOnNextListener=new HttpProgressOnNextListener<BaseDownEntity>() {
        @Override
        public void onNext(BaseDownEntity baseDownEntity) {
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
        public void updateProgress(long readLength, long countLength) {
            tvMsg.setText("提示:下载中");
            progressBar.setMax((int) countLength);
            progressBar.setProgress((int) readLength);
        }
    };

}
