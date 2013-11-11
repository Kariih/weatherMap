package com.example.weathermap;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListView;

public class ListWeatherFragment extends ListFragment {

	ArrayList<String> weatherList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		View v = inflater.inflate(R.layout.fragment_list_weather, container,false);
		
		weatherList = new ArrayList<String>();
		
		return v;
	}
}
