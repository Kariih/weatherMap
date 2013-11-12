package com.example.weathermap;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
	
public class ListWeatherFragment extends ListFragment {
	
	ArrayList<String> weatherList;
	Context context;
	StoreWeatherAdapter dbAdapter;
	
	double lat;
	double lng;
	int primaryId;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
		View v = inflater.inflate(R.layout.fragment_list_weather, container,false);
		
		TextView txtView = (TextView) v.findViewById(R.id.textView1);
		
		Bundle bundle = getArguments();
		   if(bundle != null){
		    lat = bundle.getDouble("lat", 0);
		    lng = bundle.getDouble("lng", 0);
		    primaryId = bundle.getInt("id", 0);
		    txtView.setText("Latitude: " + lat + "\tLongitude: " + lng + " id: " + primaryId);
		   }
	
		   context = getActivity().getApplicationContext();
		    dbAdapter = new StoreWeatherAdapter(context);
		    Cursor c = dbAdapter.getAllRowsWeather();
		    String date;
		    String symbol;
		    int temperature;
		    int id;
		    Weather aWeather;
		    ArrayList<Weather> weather = new ArrayList<Weather>();
		    ArrayList<String> finalData= new ArrayList<String>();
		    
		    do{
		    	id = c.getInt(c.getColumnIndex("id"));
		    	if(primaryId == id){
		    	date = c.getString(c.getColumnIndex("date_time"));
		    	temperature = c.getInt(c.getColumnIndex("temperature"));
		    	symbol = c.getString(c.getColumnIndex("symbol"));
		    	aWeather = new Weather(temperature, date, symbol);
		    	weather.add(aWeather);
		    	}else if(primaryId < id)
		    		break;
		    }while(c.moveToNext());
		    dbAdapter.close();
		    
		    for (Weather data : weather) {
				finalData.add(data.getWeatherToString());
			}

//		ImageView imageView = (ImageView) getResources().getIdentifier(symbol, "imgView", get)
//        TextView textView = (TextView) rowView.findViewById(R.id.textView);

         
		   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
			        android.R.layout.simple_list_item_1, finalData);
			    setListAdapter(adapter);
		
		return v;
	}
	
}
