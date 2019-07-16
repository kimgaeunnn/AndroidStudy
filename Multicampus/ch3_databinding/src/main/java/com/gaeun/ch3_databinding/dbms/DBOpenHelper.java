package com.gaeun.ch3_databinding.dbms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=9;

    public DBOpenHelper(Context context){
        super(context, "datadb", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableSql="create table tb_data ("+
                "_id integer primary key autoincrement," +
                "name not null," +
                "date," +
                "phone_number)";

        db.execSQL(tableSql);

        db.execSQL("insert into tb_data (name,date, phone_number) values ('홍길동','2019-07-01','010000001')");
        db.execSQL("insert into tb_data (name,date, phone_number) values ('김길동','2019-07-01','010000002')");
        db.execSQL("insert into tb_data (name,date, phone_number) values ('이길동','2019-07-01','010000003')");
        db.execSQL("insert into tb_data (name,date, phone_number) values ('박길동','2019-06-30','010000004')");
        db.execSQL("insert into tb_data (name,date, phone_number) values ('최길동','2019-06-30','010000005')");
        db.execSQL("insert into tb_data (name,date, phone_number) values ('정길동','2019-06-28','010000006')");
        db.execSQL("insert into tb_data (name,date, phone_number) values ('강길동','2019-06-28','010000007')");
        db.execSQL("insert into tb_data (name,date, phone_number) values ('조길동','2019-06-28','010000008')");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == DATABASE_VERSION){
            db.execSQL("drop table tb_data");
            onCreate(db);
        }
    }
}
