package com.gaeun.ch3_databinding.model;

public class User extends ItemVO {
    public String name;
    public String date;
    public String phone;

    @Override
    public int getType() {
        return ItemVO.TYPE_DATA;
    }
}
