package com.gaeun.ch6_retrofit;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gaeun.ch6_retrofit.databinding.ActivityMainBinding;
import com.gaeun.ch6_retrofit.databinding.ItemMainBinding;


import java.util.List;

import org.w3c.dom.Text;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

	private static final String QUERY = "travel";
	private static final String API_KEY = "MY_API_KEY";

	ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

		RetrofitService service = RetrofitFactory.create();
		// 아래처럼 interface 함수를 호출하면 네트워킹이 가능한 객체가 전달됨. (call 객체)
		// 해당 객체의 Enqueue 함수를 호출하는 순간이 네트워크를 시도하는 순감임 (결과처리 콜백 명시)
		service.getList(QUERY, API_KEY, 1, 10)
				.enqueue(new Callback<PageListModel>() {
					         @Override
					         public void onResponse(Call<PageListModel> call, Response<PageListModel> response) {
						         if (response.isSuccessful()) {
							         MyAdapter adapter = new MyAdapter(response.body().articles);
							         binding.recyclerView.setAdapter(adapter);
						         }
					         }

					         @Override
					         public void onFailure(Call<PageListModel> call, Throwable t) {

					         }
				         }
				);

	}

	class ItemViewHolder extends RecyclerView.ViewHolder {
		ItemMainBinding binding;

		public ItemViewHolder(ItemMainBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}

	class MyAdapter extends RecyclerView.Adapter<ItemViewHolder> {
		List<ItemModel> articles;

		public MyAdapter(List<ItemModel> articles) {
			this.articles = articles;
		}

		@Override
		public int getItemCount() {
			return articles.size();
		}

		@NonNull
		@Override
		public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			ItemMainBinding binding = ItemMainBinding.inflate(
					LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
			return new ItemViewHolder(binding);
		}

		@Override
		public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
			ItemModel model = articles.get(i);
			itemViewHolder.binding.setItem(model);
		}
	}

	// datat binding의 기본은 자바에서 데이터를 xml에 바인딩해서 화면 출력
	// 반대로 xml에 특정 속성(개발자 임의 이름의 속성)을 명시하고, 그 속성에 의해 자바 함수가 실행되도록 할 수 있음
	// 이 함수를 가지는 클래스는 xml에 바인딩 어있지 않아도 됨
	// 이 함수는 static으로 선언되어야함
	// 매개변수는 2개로 속성이 정의된 view객체와 속성값 이어야함!
	// -> XML View의 특정 속성값으로 추가,
	// 무언가를 처리하고 그 값으로 그 view에 적용할때 유용
	@BindingAdapter("bind:publishedAt")
	public static void publishedAt(TextView view, String date) {
		view.setText(AppUtils.getDate(date) + " at " + AppUtils.getTime(date));
	}

	@BindingAdapter("bind:urlToImage")
	public static void urlToImage(ImageView view, String url) {
		Glide.with(MyApplication.getAppContext())
				.load(url)
				.override(250, 200)
				.into(view);
	}

}


