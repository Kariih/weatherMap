package com.example.weathermap;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class placeFragment extends ListFragment {
	
	Context context;
	StoreWeatherAdapter dbAdapter;
	Weather aWeather;
	
	  public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
	
		    ArrayList<String> places = new ArrayList<String>();
		    context = getActivity().getApplicationContext();
		    dbAdapter = new StoreWeatherAdapter(context);
		    Cursor c = dbAdapter.getAllRowsPlace();
		    String date;
		    double lat;
		    double lng;
		    
		    while(c.moveToNext()){
		    	date = c.getString(c.getColumnIndex("date"));
		    	lat = c.getDouble(c.getColumnIndex("latitude"));
		    	lng = c.getDouble(c.getColumnIndex("longitude"));
		    	aWeather = new Weather(lat, lng, date);
		    	places.add(aWeather.toString());
		    	
		    }
		    
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, places);
		    setListAdapter(adapter);
		  }

		  @Override
		  public void onListItemClick(ListView l, View v, int position, long id) {
				ListWeatherFragment weatherFrag = new ListWeatherFragment();
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.replace(R.id.fragment_holder, weatherFrag);
				ft.commit();

		  }

}
