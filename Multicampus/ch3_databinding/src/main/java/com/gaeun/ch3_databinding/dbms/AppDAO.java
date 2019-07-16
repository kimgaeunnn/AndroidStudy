package com.gaeun.ch3_databinding.dbms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gaeun.ch3_databinding.model.HeaderItem;
import com.gaeun.ch3_databinding.model.ItemVO;
import com.gaeun.ch3_databinding.model.User;

import java.util.ArrayList;

public class AppDAO {
    public static ArrayList<User> initDBSelect(Context context){

        DBOpenHelper helper=new DBOpenHelper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from tb_data order by date desc", null);
        ArrayList<User> resultList=new ArrayList<>();
        while (cursor.moveToNext()){
            User user=new User();
            user.name=cursor.getString(1);
            user.date=cursor.getString(2);
            user.phone=cursor.getString(3);
            resultList.add(user);
        }
        return resultList;
    }
}
