package com.example.retrofit.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.retrofit.R;
import com.example.retrofit.activity.adapter.DownAdapter;
import com.example.retrofit.entity.DownInfo;
import com.jude.easyrecyclerview.EasyRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 多任務下載
 */
public class DownLaodActivity extends AppCompatActivity {
    EasyRecyclerView recyclerView;
    List<DownInfo> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_laod);
        recyclerView=(EasyRecyclerView)findViewById(R.id.rv);


        listData=new ArrayList<>();
        for(int i=0;i<5;i++){
//            String apkUrl="http://download.fir.im/v2/app/install/572eec6fe75e2d7a05000008?download_token=572bcb03dad2eed7c758670fd23b5ac4";
            String apkUrl="http://www.izaodao.com/app/izaodao_app.apk";
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "test"+i + ".apk");
           DownInfo apkApi=new DownInfo(apkUrl);
            apkApi.setSavePath(outputFile.getAbsolutePath());
            listData.add(apkApi);
        }


        DownAdapter adapter=new DownAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.addAll(listData);
    }
}
