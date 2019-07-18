package com.gaeun.ch5_map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class AppUtil {
	public static void checkPermission(Activity context) {
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
		}
	}

	public static void toast(Context context, String msg) {
		Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		t.show();
	}

	public static String getDateTime(long time) {
		if (time == 0)
			return "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return formatter.format(new java.util.Date(time));
	}

	public static String convertDouble(double input) {
		DecimalFormat format = new DecimalFormat(".######");
		return format.format(input);
	}
}
