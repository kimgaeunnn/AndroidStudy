package com.gaeun.ch5_map;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.gaeun.ch5_map.databinding.ActivityMainBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

	ActivityMainBinding binding;

	GoogleApiClient apiClient; // provider 준비 역할
	FusedLocationProviderClient providerClient; // 위치 획득
	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppUtil.checkPermission(this);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		apiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)   // play service 중 어떤 API사용할지
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

		providerClient = LocationServices.getFusedLocationProviderClient(this);
	}

	private void updateInfo(Location location) {
		Data data = new Data();
		data.gps_time = AppUtil.getDateTime(location.getTime());
		data.gps_location = "LAT:" + AppUtil.convertDouble(location.getLatitude())
				+ "LNG:" + AppUtil.convertDouble(location.getLongitude());
		data.gps_accuracy = location.getAccuracy()+ "m";

		binding.setData(data);
	}

	// 위치정보 획득 후 호출, 지도 제어
	private void showMap(Location location) {
		// 지도에서의 특정 위치
		LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		CameraPosition position = new CameraPosition.Builder()
				.target(latLng)
				.zoom(16f)
				.build();

		// 지도 중앙을 이동
		map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

		// 이전 marker 지워야 겹쳐서 안그림
		map.clear();

		// mark 올리기
		map.addMarker(new MarkerOptions().position(latLng).title("바로 여기")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
	}

	@Override
	protected void onResume() {
		super.onResume();

		apiClient.connect(); // location provider 준비, 알아서 callback을 호출해 결과를 알려줌

		if (map == null) {
			// map 객체를 callback으로 받는다.
			// callack을 이용하지 않고 map 객체 획득시 실제 map 이용준비가 안된 상태일 수도 있어서 문제 발생
			((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_view))
					.getMapAsync(this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (apiClient.isConnected()) {
			apiClient.disconnect();
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		UiSettings settings = map.getUiSettings();
		settings.setZoomControlsEnabled(true);
	}

	// Provider 이용 가능시 호출
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		// 위치정보 획득
		// Permission check 해야한다 정도의 경고, 위에서 처리하고 있어서 상관 없음.
		// 결과값은 callback으로!
		providerClient.getLastLocation()
				.addOnSuccessListener(new OnSuccessListener<Location>() {
					@Override
					public void onSuccess(Location location) {
						if (location != null) {
							updateInfo(location);
							showMap(location);
						}
					}
				});
	}

	@Override
	public void onConnectionSuspended(int i) {
		AppUtil.toast(this,"onConnectionSuspended");
	}

	// 현재 디바이스에 가용 가능한 Provider 없는 경우
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		AppUtil.toast(this, "onConnectionFailed");
	}
}
