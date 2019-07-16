package com.gaeun.ch3_databinding;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gaeun.ch3_databinding.databinding.ActivityMainBinding;
import com.gaeun.ch3_databinding.dbms.AppDAO;
import com.gaeun.ch3_databinding.model.User;

public class MainActivity extends AppCompatActivity {

	// 라이브러리에서 제공되는 class가 아니라, xml 파일명에 해당되는 클래스가 자동으로 만들어진것.
	// xml에 선언한 view 변수가 이 안에 모두 포함된다.
	ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		selectUser((User) AppDAO.initDBSelect(this).get(0));
	}

	private void selectUser(User user) {
		binding.setSelect(user);
	}
}
