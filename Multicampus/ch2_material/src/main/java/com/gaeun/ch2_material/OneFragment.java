package com.gaeun.ch2_material;


import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {


	RecyclerView recyclerView;


	public OneFragment() {
		// Required empty public constructor
	}

	// fragment 화면을 준비하기 위해 자동 호출!
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_one, container, false);

		// 항목 구성 데이터
		List<String> list = new ArrayList<>();
		for(int i=0; i < 20; i++) {
			list.add("Item =" + i);
		}

		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(new MyAdapter(list));
		recyclerView.addItemDecoration(new MyDecoration());

		return recyclerView;
	}

	// 항목을 구성하기 위해 필요한 view를 findViewById해서 가지고 있는 역할
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView titleView;
		MyViewHolder(View root) {
			super(root);
			titleView = root.findViewById(android.R.id.text1);
		}
	}

	// 항목을 구성하는 역할
	class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
		List<String> list; // 항목 구성 집합 데이터
		MyAdapter(List<String> list) {
			this.list = list;
		}

		// 항목 구성을 위한 view 획득해서 가지고 있는 Holder 준비
		// 이 함수에서 리턴시킨 holder를 내부적으로 메모리에 유지했다가 재사용 해준다.
		@NonNull
		@Override
		public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			View root = LayoutInflater.from(viewGroup.getContext())
					.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
			return new MyViewHolder(root);
		}

		// 각 항목 구성
		@Override
		public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
			myViewHolder.titleView.setText(list.get(i));
		}

		@Override
		public int getItemCount() {
			return list.size();
		}
	}

	class MyDecoration extends RecyclerView.ItemDecoration {
		@Override
		public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
			super.onDrawOver(c, parent, state);

			// 화면 사이즈 획득
			int width = parent.getWidth();
			int height = parent.getHeight();

			// 이미지 획득
			Drawable dr = getActivity().getResources().getDrawable(R.drawable.kbo);
			int drWidth = dr.getIntrinsicWidth();
			int drHeight = dr.getIntrinsicHeight();

			int left = width / 2 -  drWidth / 2;
			int top = height / 2 - drHeight / 2;

			// 이미지 그리기
			c.drawBitmap(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.kbo), left, top, null);
		}

		// 각각의 항목을 꾸미기 위해 호출
		@Override
		public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
			// 항목 index 값 획득
			int index = parent.getChildAdapterPosition(view) + 1;
			if (index % 3 == 0) {
				outRect.set(20, 20, 20, 60);
			} else {
				outRect.set(20, 20, 20, 20);
			}

			view.setBackgroundColor(0xFFECE9E9);
			ViewCompat.setElevation(view, 20.0f);
		}
	}

}
