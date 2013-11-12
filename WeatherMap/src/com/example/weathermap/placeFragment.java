package com.example.weathermap;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class placeFragment extends ListFragment {
	
	Context context;
	StoreWeatherAdapter dbAdapter;
	Weather aWeather;
    double lat;
    double lng;
    int primaryKeyId;
	Bundle bundle;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		View v = inflater.inflate(R.layout.fragment_list_weather, container,false);


		    bundle = new Bundle();
		    ArrayList<Weather> places = new ArrayList<Weather>();
		    context = getActivity().getApplicationContext();
		    
		    File database = getActivity().getApplicationContext().getDatabasePath("weatherDb");
		    
			if (database.exists()) {
				System.out.println("funnet");
	

		    dbAdapter = new StoreWeatherAdapter(context);
		    Cursor c = dbAdapter.getAllRowsPlace();
		    String date;
		    
		    do{
		    	date = c.getString(c.getColumnIndex("date"));
		    	lat = c.getDouble(c.getColumnIndex("latitude"));
		    	lng = c.getDouble(c.getColumnIndex("longitude"));
		    	primaryKeyId  = c.getInt(c.getColumnIndex("id"));
		    	aWeather = new Weather(lat, lng, date, primaryKeyId);
		    	places.add(aWeather);
		    }while(c.moveToNext());
		    dbAdapter.close();
		    
			} else {
				System.out.println("ikke funnet");
			}
		    
		    ArrayAdapter<Weather> adapter = new ArrayAdapter<Weather>(getActivity(),
		        android.R.layout.simple_list_item_1, places);
		    setListAdapter(adapter);
			return v;
		  }
	
		  @Override
		  public void onListItemClick(ListView l, View v, int position, long id) {
			  
			  	Weather clickedPlace = (Weather) l.getItemAtPosition(position);
			  	lat = clickedPlace.getLat();
			  	lng = clickedPlace.getLon();
			  	primaryKeyId = clickedPlace.getId();
				ListWeatherFragment weatherFrag = new ListWeatherFragment();
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				bundle.putDouble("lat", lat);
				bundle.putDouble("lng", lng);
				bundle.putInt("id", primaryKeyId);
				weatherFrag.setArguments(bundle);
				ft.replace(R.id.fragment_holder, weatherFrag);
				ft.commit();


		  }

}
