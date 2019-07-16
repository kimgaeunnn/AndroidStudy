package com.gaeun.ch3_databinding.recycler;

import android.support.v7.widget.RecyclerView;

import com.gaeun.ch3_databinding.databinding.ItemHeaderBinding;

// Recycler View의 Adapter에서 이용하는 항목을 위한 view, find해서 가지고있는 역할
public class HeaderViewHolder extends RecyclerView.ViewHolder {

	// Holder의 역할이 view를 find 하는 역할인데, Binding이 이미 가지고 있기 때문에 할필요 없쥬?
	public ItemHeaderBinding binding;

	public HeaderViewHolder(ItemHeaderBinding binding) {
		super(binding.getRoot());   // getRoot: View 계층의 최상위 객체를 얻음

		this.binding = binding;
	}
}
