package com.gaeun.ch3_databinding.recycler;

import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gaeun.ch3_databinding.model.ItemVO;

import java.util.List;

public class MyDecoration extends RecyclerView.ItemDecoration{
    List<ItemVO> list;
    public MyDecoration(List<ItemVO> list){
        this.list=list;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int index=parent.getChildAdapterPosition(view);
        ItemVO itemVO=list.get(index);
        if(itemVO.getType()== ItemVO.TYPE_DATA){
            view.setBackgroundColor(0xFFFFFFFF);
            ViewCompat.setElevation(view, 10.0f);
        }
        outRect.set(20, 10, 20, 10);
    }
}