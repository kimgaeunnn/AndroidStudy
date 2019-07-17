package com.gaeun.ch4_aidl_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayMap;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaeun.ch4_aidl_client.databinding.ActivityMainBinding;

import com.gaeun.ch4_aidl.IPlayService;

public class MainActivity extends AppCompatActivity {

	boolean runThread;

	IPlayService pService;
	ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		binding.setTitle("music.mp3");
		binding.setHandler(this);

		binding.play.setEnabled(false);
		binding.stop.setEnabled(false);


		// Intent로 외부 앱과 연결
		Intent intent = new Intent("com.multi.ACTION_PLAY");
		// BindService의 intent에는 꼭! Package 명시해야함.
		intent.setPackage("com.gaeun.ch4_aidl");
		// 넘어온 객체는 connection의 함수 호출로 전달
		bindService(intent, connection, Context.BIND_AUTO_CREATE);

	}

	public void onPlay(View view) {
		if (pService != null) {
			try {
				pService.start();
				runThread = true;

				binding.progress.setMax(pService.getMaxDuration());

				ProgressThread thread = new ProgressThread();
				thread.start();

				binding.play.setEnabled(false);
				binding.stop.setEnabled(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onStop(View view) {
		if (pService != null) {
			try {
				pService.stop();
				runThread = false;

				binding.play.setEnabled(true);
				binding.stop.setEnabled(false);
				binding.progress.setProgress(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pService = IPlayService.Stub.asInterface(service);

			binding.play.setEnabled(true);

			try {
				binding.progress.setMax(pService.getMaxDuration());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			pService = null;
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindService(connection);
		runThread = false;
	}


	class ProgressThread extends Thread {
		@Override
		public void run() {
			while (runThread) {
				try {
					binding.progress.setProgress(pService.currentPosition());
					SystemClock.sleep(1000);
					if (pService.currentPosition() == binding.progress.getMax()) {
						runThread = false;
					}
				} catch (Exception e) {

				}
			}
		}
	}


}


