package com.gaeun.ch3_databinding.recycler;

import android.support.v7.widget.RecyclerView;

import com.gaeun.ch3_databinding.databinding.ItemDataBinding;

public class UserViewHolder extends RecyclerView.ViewHolder {

	public ItemDataBinding binding;

	public UserViewHolder(ItemDataBinding binding) {
		super(binding.getRoot());

		this.binding = binding;
	}

}
