package com.example.ch7_paging;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ch7_paging.datasource.MyDataFactory;
import com.example.ch7_paging.model.ItemModel;


public class MyViewModel extends ViewModel {
    //add~~~~~~~~~~~
    private LiveData<PagedList<ItemModel>> itemLiveData;
    PagedList.Config pagedListConfig;
    public MyViewModel(){
        //add~~~~~~~~~~~~
        //PagedList 설정객체..
        pagedListConfig =
                (new PagedList.Config.Builder())
                        .setInitialLoadSizeHint(3)//초기 획득 항목 수...
                        .setPageSize(3)//그 다음 페이지 부터 획득하는 항목 수...
                        .build();
    }

    public LiveData<PagedList<ItemModel>> getNews() {

        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            //add~~~~~~~~~~~~~~~~~~~~~~
            //DataSource.Factory 상속 클래스. DataSource를 생성해주는 역활
            MyDataFactory dataFactory = new MyDataFactory();
            itemLiveData = (new LivePagedListBuilder(dataFactory, pagedListConfig))
                    .build();
            return itemLiveData;
        } else {
            //add~~~~~~~~~~~~~~~~~~
            DataSource.Factory<Integer, ItemModel>  factory = MyApplication.dao.getAll();
            itemLiveData=(new LivePagedListBuilder(factory, pagedListConfig )).build();
            return itemLiveData;
        }
    }

}
