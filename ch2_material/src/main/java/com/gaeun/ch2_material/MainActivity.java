package com.gaeun.ch2_material;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	ViewPager viewPager;
	Toolbar toolbar;

	// drawer
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle toggle;
	boolean isDrawerOpened;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = findViewById(R.id.toolbar);
		viewPager = findViewById(R.id.viewpager);

		setSupportActionBar(toolbar);
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));


		// drawer
		drawerLayout = findViewById(R.id.drawer);
		// ActionBarDrawerToggle을 상속받아서, 서브클래스를 만들지 않아도, Toggle 제공 가능
		// drawer open, close 상태시, 이벤트 처리가 필요한 경우, 서브클래스로 적용
		// 이벤트 처리하지 않아도 유저가 open, close는 된다.
		// 생성자 매개변수의 문자열은 화면과 전혀 상관없다. 현 drawer의 상태를 표현하기 위한 문자열
		toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				isDrawerOpened = true;
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				isDrawerOpened = false;
			}
		};

		drawerLayout.addDrawerListener(toggle);
		// 햄버거 아이콘
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		toggle.syncState();

		// TabLayout
		TabLayout tabLayout = findViewById(R.id.tabs);
		// 아래의 ViewPager와 연결만으로도 상호 연동이 됨
		// tabButton을 꾸미는건 (문자열) 코드에서 직접 해도 되고,
		// viewPager와 연결되어있다면, 해당 viewPager화면 title 문자열을 그대로 가져옴.
		tabLayout.setupWithViewPager(viewPager);
	}

	// menu event 처리함수
	// toggle 클릭이 내부적으로 메뉴 이벤트로 취급되지 않게 하기 위함
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (toggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// back button 이벤트, onKeyDown()을 이용해도 되고, backButton 이벤트만을 위한 함수
	@Override
	public void onBackPressed() {
		if (isDrawerOpened) drawerLayout.closeDrawers();
		else super.onBackPressed();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	// ViewPager는 대표적인 AdapterView 이다 항목하나가 화면하나로!  >> adapter를 만들어주자
	// 순서대로 손가락 따라서!
	// FragmentStatePagerAdapter 상속받아 만들면, Fragment 제어 자동화
	class MyAdapter extends FragmentStatePagerAdapter {
		List<Fragment> fragments = new ArrayList<>();

		String[] titles = new String[]{"tab1", "tab2", "tab3"};

		public MyAdapter(FragmentManager fm) {
			// 상위클래스에 fragmentManager 전달, 상위클래스에서 알아서!
			// Transaction 도출해서, fragment 제어해준다.
			super(fm);

			fragments.add(new OneFragment());
			fragments.add(new TwoFragment());
			fragments.add(new ThreeFragment());
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		// 항목을 위한 화면 결정하기 위해서 자동 호출!
		@Override
		public Fragment getItem(int i) {
			return fragments.get(i);
		}

		// ViewPagerTitleStrip, ViewPagerTabStrip을 사용한 경우, 아래의 함수 호출, 문자열 이용함. (현재 코드는 이경우는 아님)
		// TabLayout과 viewPager가 연결되어있는경우, 아래 함수 호출로 tab button 문자열을 사용!
		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}
	}
}
