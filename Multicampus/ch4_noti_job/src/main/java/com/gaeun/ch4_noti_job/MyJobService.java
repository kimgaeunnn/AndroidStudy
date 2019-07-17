package com.gaeun.ch4_noti_job;

import android.annotation.TargetApi;
import android.app.*;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

	boolean runFlag = true;

	public MyJobService() {

	}

	@Override
	public boolean onStartJob(JobParameters jobParameters) {
		Log.d("gaeun", "onStartJob is Called !");

		NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification.Builder builder = null;

		// API Level26 부터는 Builder 를 만들때 꼭 Channel을 적용해야함 (안하면 안돌쥬?)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel("NotiChannelID", "gaeunChannel", NotificationManager.IMPORTANCE_DEFAULT);

			channel.setDescription("Description");
			manager.createNotificationChannel(channel);

			builder = new Notification.Builder(this, "NotiChannelID");
		} else {
			// API 26 이하인 경우 channel 적용하면 Runtime Error
			builder = new Notification.Builder(this);
		}

		builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
		builder.setContentTitle("Wi-Fi 연결 감지");
		builder.setContentText("Wi-Fi에 연결되었습니다.");
		builder.setAutoCancel(true);

		// Touch Event 의뢰 (PendingIntent에 내가 만든 intent를 담아 날린다)
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);

		// Noti 발생
		manager.notify(1, builder.build());

		return true;
	}

	@Override
	public boolean onStopJob(JobParameters jobParameters) {
		return true;
	}
}
