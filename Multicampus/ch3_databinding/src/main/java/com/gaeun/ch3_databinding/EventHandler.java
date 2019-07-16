package com.gaeun.ch3_databinding;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class EventHandler {

	// Event Binding
	// 매개변수 선언 (Listener 방식) >> 함수 참조 방식으로 받으려면, 실제 콜백 함수와 매개변수 동일해야함.
	public  static void onPhoneCall(Context context, String phone) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
		context.startActivity(intent);
	}
}
