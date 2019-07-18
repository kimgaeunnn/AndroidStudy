package com.gaeun.ch6_volley;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gaeun.ch6_volley.databinding.ActivityMainBinding;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

	ActivityMainBinding binding;
	HashMap<String, Bitmap> imgMap; // 이미지 캐싱
	RequestQueue queue;
	ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		imgMap = new HashMap<>();
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		queue = Volley.newRequestQueue(this);

		// Image 획득시 매개변수로 이 객체를 전달하면, 서버연동전에 함수 호출하여
		// 캐싱되어있는 image를 사용하도록, 없으면 서버연동!
		imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
			// 서버 요청 전에 호출, 리턴값이 Null 이면 서버 연동
			// null 이 아닌 경우 리턴된 Bitmap 그대로 이용
			@Override
			public Bitmap getBitmap(String url) {
				Bitmap bitmap = imgMap.get(url);
				if (bitmap != null) {
					return bitmap;
				} else return null;
			}

			// 서버에서 이미지 획득시 호출, 캐싱하라고!!
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				imgMap.put(url, bitmap);
			}
		});

		// json data callback
		Response.Listener resultListener = new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					Data data = new Data();
					data.title = response.getString("title");
					data.date = response.getString("date");
					data.content = response.getString("content");

					binding.setData(data);

					String imageFile = response.getString("file");
					if (imageFile != null && !imageFile.equals("")) {
						// 다운로드 및 바인딩을 통한 화면 출력
						binding.image.setImageUrl("http://10.5.6.100:8080/" + imageFile, imageLoader);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://10.5.6.100:8080/test.json", null, resultListener,
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});

		// 네트워크 시도
		queue.add(request);
	}
}
