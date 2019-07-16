package com.gaeun.ch3_databinding.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gaeun.ch3_databinding.databinding.ItemDataBinding;
import com.gaeun.ch3_databinding.databinding.ItemHeaderBinding;
import com.gaeun.ch3_databinding.model.HeaderItem;
import com.gaeun.ch3_databinding.model.ItemVO;
import com.gaeun.ch3_databinding.model.User;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	Context context;
	List<ItemVO> items; // 항목 구성 집합 데이터

	public MyAdapter(Context context, List<ItemVO> items) {
		this.context = context;
		this.items = items;
	}

	// 항목의 타입이 여러가지인 경우, 항목의 타입을 식별해서 이용해야함.
	// 아래의 함수를 override 해서 각 index의 항목 타입이 무엇인지를 return
	@Override
	public int getItemViewType(int position) {
		return items.get(position).getType();
	}

	// 항목을 구성하기 위한 Holder(화면)을 준비, 타입에 다르게 준비해야함
	// 위의 함수에서 Return 시킨 값이 두번째 매개변수로 전달됨
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		if (i == ItemVO.TYPE_HEADER) {
			// Activity의 layout.xml뿐 아니라, Fragment, ListView, RecyclerView 등, 특정 View 내부에서 사용하는 Layout.xml에도 DataBinding 적용가능
			// Activity에서 사용하는 xml은 DataBindingUtil.setContentView() 함수를 이용하며
			// 특정 View를 xml inflate 및 DataBinding 적용은 아래처럼 !
			ItemHeaderBinding binding = ItemHeaderBinding.inflate(
					LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

			return new HeaderViewHolder(binding);
		} else {
			ItemDataBinding binding = ItemDataBinding.inflate(
					LayoutInflater.from(viewGroup.getContext()), viewGroup, false);

			return new UserViewHolder(binding);
		}
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		ItemVO itemVO = items.get(i);

		if (itemVO.getType() == ItemVO.TYPE_HEADER) {
			HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
			HeaderItem item = (HeaderItem) itemVO;

			holder.binding.setData(item.headerTitle);
		} else {
			UserViewHolder holder = (UserViewHolder) viewHolder;
			User user = (User) itemVO;

			holder.binding.setUser(user);
		}
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}
