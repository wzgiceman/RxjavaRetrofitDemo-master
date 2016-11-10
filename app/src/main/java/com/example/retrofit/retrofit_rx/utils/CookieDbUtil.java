package com.example.retrofit.retrofit_rx.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.retrofit.MyApplication;
import com.example.retrofit.retrofit_rx.download.DaoMaster;
import com.example.retrofit.retrofit_rx.download.DaoSession;
import com.example.retrofit.retrofit_rx.download.HttpDownManager;
import com.example.retrofit.retrofit_rx.http.cookie.CookieResulte;
import com.example.retrofit.retrofit_rx.http.cookie.CookieResulteDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * 数据缓存
 * 数据库工具类-geendao运用
 * Created by WZG on 2016/10/25.
 */

public class CookieDbUtil {
    private static CookieDbUtil db;
    private final static String dbName = "tests_db";
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;


    public CookieDbUtil() {
        context= MyApplication.app;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName);
    }


    /**
     * 获取单例
     * @return
     */
    public static CookieDbUtil getInstance() {
        if (db == null) {
            synchronized (HttpDownManager.class) {
                if (db == null) {
                    db = new CookieDbUtil();
                }
            }
        }
        return db;
    }


    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }


    public void saveCookie(CookieResulte info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResulteDao downInfoDao = daoSession.getCookieResulteDao();
        downInfoDao.insert(info);
    }

    public void updateCookie(CookieResulte info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResulteDao downInfoDao = daoSession.getCookieResulteDao();
        downInfoDao.update(info);
    }

    public void deleteCookie(CookieResulte info){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResulteDao downInfoDao = daoSession.getCookieResulteDao();
        downInfoDao.delete(info);
    }


    public CookieResulte queryCookieBy(String  url) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResulteDao downInfoDao = daoSession.getCookieResulteDao();
        QueryBuilder<CookieResulte> qb = downInfoDao.queryBuilder();
        qb.where(CookieResulteDao.Properties.Url.eq(url));
        List<CookieResulte> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public List<CookieResulte> queryCookieAll() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CookieResulteDao downInfoDao = daoSession.getCookieResulteDao();
        QueryBuilder<CookieResulte> qb = downInfoDao.queryBuilder();
        return qb.list();
    }
}
