package com.gaeun.ch4_aidl;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;

public class PlayService extends Service {

	MediaPlayer player;     // 음원, 영상 play 용, MeiaRecoder: 소리, 영상 녹화

	public PlayService() {

	}

	@Override
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		player.release();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// bind Service 쪽에 전달되는 것은 실제 업무처리하는 객체가 아니라,
		// AIDL 통신을 대행하는 Stub 임
		return new IPlayService.Stub() {

			@Override
			public int currentPosition() throws RemoteException {
				if (player.isPlaying()) {
					return player.getCurrentPosition();
				} else {
					return 0;
				}
			}

			@Override
			public int getMaxDuration() throws RemoteException {
				if (player.isPlaying()) {
					return player.getDuration();
				} else {
					return 0;
				}
			}

			@Override
			public void start() throws RemoteException {
				if (!player.isPlaying()) {
					player = MediaPlayer.create(PlayService.this, R.raw.music);

					try {
						player.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void stop() throws RemoteException {
				if (player.isPlaying()) {
					player.stop();
				}
			}
		};
	}
}
