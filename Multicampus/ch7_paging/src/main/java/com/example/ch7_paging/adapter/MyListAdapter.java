package com.example.ch7_paging.adapter;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.ch7_paging.databinding.ItemMainBinding;
import com.example.ch7_paging.model.ItemModel;


public class MyListAdapter extends PagedListAdapter<ItemModel, MyListAdapter.ItemViewHolder> {

    //add~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public static DiffUtil.ItemCallback<ItemModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ItemModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MyListAdapter(Context context) {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        ItemMainBinding binding = ItemMainBinding.inflate(layoutInflater, parent, false);
        return new ItemViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //add~~~~~~~~~~~~~~~~~~~~~~~~
        //PagedListAdapter 에서 제공하는 함수.. 항목 데이터 획득
        ItemModel article = getItem(position);
        holder.binding.setData(article);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemMainBinding binding;
        public ItemViewHolder(ItemMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
