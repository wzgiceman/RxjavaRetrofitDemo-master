package com.example.retrofit.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.retrofit.R;
import com.example.retrofit.activity.adapter.DownAdapter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.download.DownInfo;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.DbDownUtil;

import java.io.File;
import java.util.List;

/**
 * 多任務下載
 */
public class DownLaodActivity extends AppCompatActivity {
    List<DownInfo> listData;
    DbDownUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_laod);
        initResource();
        initWidget();
    }

    /*数据*/
    private void initResource() {
        dbUtil = DbDownUtil.getInstance();
        listData = dbUtil.queryDownAll();
        /*第一次模拟服务器返回数据掺入到数据库中*/
        if (listData.isEmpty()) {
            for (int i = 0; i < 4; i++) {
                File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "test" + i + ".mp4");
                DownInfo apkApi = new DownInfo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                apkApi.setId(i);
                apkApi.setUpdateProgress(true);
                apkApi.setSavePath(outputFile.getAbsolutePath());
                dbUtil.save(apkApi);
            }
            listData = dbUtil.queryDownAll();
        }
    }

    /*加载控件*/
    private void initWidget() {
        EasyRecyclerView recyclerView = (EasyRecyclerView) findViewById(R.id.rv);
        DownAdapter adapter = new DownAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.addAll(listData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*记录退出时下载任务的状态-复原用*/
        for (DownInfo downInfo : listData) {
            dbUtil.update(downInfo);
        }
    }

}
