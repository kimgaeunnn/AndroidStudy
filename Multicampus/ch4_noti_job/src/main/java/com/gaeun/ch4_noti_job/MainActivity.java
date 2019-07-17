package com.gaeun.ch4_noti_job;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gaeun.ch4_noti_job.databinding.ActivityMainBinding;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

	ActivityMainBinding binding;

	JobScheduler jobScheduler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		binding.setHandler(this);

		jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
	}

	public void onAddJob(View view) {
		ComponentName componentName = new ComponentName(this, MyJobService.class);

		// 시스템에 등록시키기 위한 정보
		JobInfo.Builder builder = new JobInfo.Builder(1, componentName);
		builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // WIFI Enable 상태
		builder.setRequiresCharging(true);

		JobInfo jobInfo = builder.build();

		jobScheduler.schedule(jobInfo);

	}
}
