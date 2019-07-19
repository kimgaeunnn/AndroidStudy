package com.example.ch7_paging.room;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.ch7_paging.model.ItemModel;


@Dao
public interface ArticleDAO {

    //add~~~~~~~~~~~~~~~~~~~~~~
    @Query("SELECT * FROM article")
    DataSource.Factory<Integer, ItemModel> getAll();

    @Insert
    void insertAll(ItemModel... users);

    @Query("DELETE FROM article")
    void deleteAll();
}
