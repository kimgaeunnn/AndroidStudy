package com.gaeun.ch3_databinding.util;

import com.gaeun.ch3_databinding.model.HeaderItem;
import com.gaeun.ch3_databinding.model.ItemVO;
import com.gaeun.ch3_databinding.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdapterUtil {
    private static final String TODAY="2019-07-01";
    private static final String YESTERDAY="2019-06-30";
    public static List<ItemVO> makeAdapterData(ArrayList<User> users){
        ArrayList<ItemVO> list=new ArrayList<>();
        String cursorDay=null;
        for(User user : users) {
            if (!user.date.equals(cursorDay)) {
                cursorDay = user.date;
                if (cursorDay.equals(TODAY)) {
                    HeaderItem item = new HeaderItem();
                    item.headerTitle = "오늘";
                    list.add(item);
                } else if (cursorDay.equals(YESTERDAY)) {
                    HeaderItem item = new HeaderItem();
                    item.headerTitle = "어제";
                    list.add(item);
                } else {
                    HeaderItem item = new HeaderItem();
                    item.headerTitle = "이전";
                    list.add(item);
                }
            }
            list.add(user);
        }

        return list;
    }
}
