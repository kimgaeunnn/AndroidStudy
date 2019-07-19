package com.example.ch7_paging;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.ch7_paging.retrofit.RetrofitFactory;
import com.example.ch7_paging.retrofit.RetrofitService;
import com.example.ch7_paging.room.AppDatabase;
import com.example.ch7_paging.room.ArticleDAO;


public class MyApplication extends Application {

    public static Context context;
    public static ArticleDAO dao;
    public static RetrofitService networkService;
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "database-name").build();
        dao = db.articleDao();
        networkService = RetrofitFactory.create();
    }
}
