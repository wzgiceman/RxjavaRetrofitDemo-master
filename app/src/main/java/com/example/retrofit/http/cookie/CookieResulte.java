package com.example.retrofit.http.cookie;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * post請求緩存数据
 * Created by WZG on 2016/10/26.
 */
@Entity
public class CookieResulte {
    @Id
    private long id;
    /*url*/
    private String url;
    /*返回结果*/
    private String resulte;

    public CookieResulte(String url, String resulte) {
        this.url = url;
        this.resulte = resulte;
    }

    @Generated(hash = 549012884)
    public CookieResulte(long id, String url, String resulte) {
        this.id = id;
        this.url = url;
        this.resulte = resulte;
    }
    @Generated(hash = 2104390000)
    public CookieResulte() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getResulte() {
        return this.resulte;
    }
    public void setResulte(String resulte) {
        this.resulte = resulte;
    }
}
