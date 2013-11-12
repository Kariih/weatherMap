package com.example.weathermap;

import java.io.File;

import com.google.android.gms.maps.GoogleMap;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

public class MainActivity extends Activity {

	StoreWeatherAdapter dbAdapter;
	FragmentMap mapfrag;
	GoogleMap map;
	Context context; 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		mapfrag = new FragmentMap();
		tabs();
		
		File database= getApplicationContext().getDatabasePath("weatherDb");
		if (!database.exists()) {
			System.out.println("lager database");
			dbAdapter = new StoreWeatherAdapter(context);
			dbAdapter.close();
		} else {
			System.out.println("eksisterer");
		}
		
		
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
