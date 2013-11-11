package com.example.weathermap;

import java.util.ArrayList;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static StoreWeatherAdapter dbAdapter;
	FragmentMap mapfrag;
	private CameraPosition cp;

	StoreWeatherAdapter weatherAdapter;
	public static ArrayList<String> placeList = new ArrayList<String>();
	public static ArrayList<Weather> markerList = new ArrayList<Weather>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentMap mapInfo = new FragmentMap();
		tabs();

	}

	public void tabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		FragmentMap mapFrag = new FragmentMap();
		Tab mapTab = actionBar.newTab();
		mapTab.setText("Map");
		mapTab.setTabListener(new ThisTabListener(mapFrag));

		placeFragment pFragment = new placeFragment();
		Tab weatherTab = actionBar.newTab();
		weatherTab.setText("Weather");
		weatherTab.setTabListener(new ThisTabListener(pFragment));

		actionBar.addTab(mapTab);
		actionBar.addTab(weatherTab);

	}

	public void onPause() {
		mapfrag.onPause();
		super.onPause();

		cp = mapfrag.map.getCameraPosition();
		mapfrag = null;
	}
	private void addMapMarkers() {
		for (Weather marker : markerList) {
			LatLng laln = new LatLng(marker.getLat(), marker.getLon());
			mapfrag.addMarker(laln, marker.getWeatherType());
	}
	}
		
	private class ThisTabListener implements ActionBar.TabListener {

		Fragment fragment;

		public ThisTabListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_holder, fragment);
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_holder, fragment);

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {


		}

	}
}
