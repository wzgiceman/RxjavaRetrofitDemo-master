package com.example.retrofit.activity.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.retrofit.R;
import com.example.retrofit.entity.DownInfo;
import com.example.retrofit.http.HttpDownManager;
import com.example.retrofit.listener.HttpProgressOnNextListener;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by WZG on 2016/10/21.
 */

public class DownHolder extends BaseViewHolder<DownInfo> implements View.OnClickListener{
    private TextView tvMsg;
    private NumberProgressBar progressBar;
    private     DownInfo apkApi;
    private HttpDownManager manager;

    public DownHolder(ViewGroup parent) {
        super(parent, R.layout.view_item_holder);
        manager=HttpDownManager.getInstance();
        $(R.id.btn_rx_down).setOnClickListener(this);
        $(R.id.btn_rx_pause).setOnClickListener(this);
        progressBar=$(R.id.number_progress_bar);
        tvMsg=$(R.id.tv_msg);
    }

    @Override
    public void setData(DownInfo data) {
        super.setData(data);
        data.setListener(httpProgressOnNextListener);
        apkApi=data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rx_down:
                downApk();
                break;
            case R.id.btn_rx_pause:
                pause();
                break;
        }
    }

    /*下载处理 6.0以后的手机需要加入权限判断*/
    private void downApk(){
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
            Toast.makeText(getContext(),baseDownEntity.getSavePath(),Toast.LENGTH_SHORT).show();
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
            Log.e("tag","---->暂停了");
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
}