package com.example.ch7_paging.datasource;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;


import com.example.ch7_paging.MyApplication;
import com.example.ch7_paging.model.ItemModel;
import com.example.ch7_paging.model.PageListModel;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyDataSource extends PageKeyedDataSource<Long, ItemModel> {

    private static final String QUERY = "travel";
    private static final String API_KEY = "MY_API_KEY";

    //초기 데이터 획득
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params,
                            @NonNull LoadInitialCallback<Long, ItemModel> callback) {
        Log.i("kkang", "loadInitial......   Count " + params.requestedLoadSize);

        MyApplication.networkService.getList(QUERY, API_KEY, 1, params.requestedLoadSize)
                .enqueue(new Callback<PageListModel>() {
                    @Override
                    public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
                        if (response.isSuccessful()) {
                            Log.d("kkang", new Gson().toJson(response.raw()));
                            Log.d("kkang", new Gson().toJson(response.body()));
                            callback.onResult(response.body().articles, null, 2L);

                            new InsertDataThread(response.body().articles).start();
                        }
                    }

                    @Override
                    public void onFailure(Call<PageListModel> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params,
                           @NonNull LoadCallback<Long, ItemModel> callback) {
        //params 에 key 가 매핑되지 전에 호출
        Log.d("kkang", "loadBefore............ " + params.key + " Count " + params.requestedLoadSize);
    }

    //페이지 데이터 획득을 위해 반복 호출
    //param 정보에 그 다음 페이지 번호가 자동으로 엡데이트 되어 전달된다.
    @Override
    public void loadAfter(@NonNull LoadParams<Long> params,
                          @NonNull LoadCallback<Long, ItemModel> callback) {
        Log.d("kkang", "loadAfter............ " + params.key + " Count " + params.requestedLoadSize);
        MyApplication.networkService.getList(QUERY, API_KEY, params.key, params.requestedLoadSize).enqueue(new Callback<PageListModel>() {
            @Override
            public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
                if(response.isSuccessful()) {
                    long nextKey = (params.key == response.body().totalResults) ? null : params.key+1;
                    callback.onResult(response.body().articles, nextKey);
                }
            }

            @Override
            public void onFailure(Call<PageListModel> call, Throwable t) {
            }
        });
    }

    class InsertDataThread extends Thread {
        List<ItemModel> articles;
        public InsertDataThread(List<ItemModel> articles){
            this.articles=articles;
        }
        @Override
        public void run() {
            MyApplication.dao.deleteAll();
            MyApplication.dao.insertAll(articles.toArray(new ItemModel[articles.size()]));
        }
    }
}
