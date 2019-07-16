package com.gaeun.ch3_databinding.model;

public class HeaderItem extends ItemVO {
    public String headerTitle;

    @Override
    public int getType() {
        return ItemVO.TYPE_HEADER;
    }
}
