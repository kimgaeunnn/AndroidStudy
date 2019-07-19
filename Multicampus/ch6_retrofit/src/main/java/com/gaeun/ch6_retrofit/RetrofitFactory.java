package com.gaeun.ch6_retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
	private static String BASE_URL = "https://newsapi.org";

	public static RetrofitService create() {
		// Retrofit 객체는 app 전역에 하나만 유지해도 된다.
		// 기초 정보 등록 목적임 (baseURL, converter 등)
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		// Retrofit 객체에 개발자가 만든 interface를 등록하고 그대로 씀!
		// 네트워킹을 대행하는 클래스 객체를 만들어 사용하는 구조
		return retrofit.create(RetrofitService.class);
	}
}
