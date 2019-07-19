package com.example.ch7_paging.datasource;

import android.arch.paging.DataSource;

public class MyDataFactory extends DataSource.Factory {
    //LivePagedListBuilder 에 의해 자동 호출되며 데이터 획득의 실제 작업을 위한 DataSource 객체를 리턴시켜 주면 된다.
    @Override
    public DataSource create() {
        return new MyDataSource();
    }
}
