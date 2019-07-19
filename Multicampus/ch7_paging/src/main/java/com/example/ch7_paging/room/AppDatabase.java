package com.example.ch7_paging.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.ch7_paging.model.ItemModel;


@Database(entities = {ItemModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ArticleDAO articleDao();
}
