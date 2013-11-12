package com.example.weathermap;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.internal.p;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends MapFragment implements OnMapClickListener {

	GoogleMap map;
	double clickedLat;
	double clickedLng;
	JsonTask jasonTask;
	String pictureSymbolName;
	String resorce;
	Context context;
	String symbolIcon;
	public static String markerMap;
	StoreWeatherAdapter dbAdapter;
	Cursor c;

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		context = getActivity().getApplicationContext();
		map = getMap();
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(this);
		File database= getActivity().getApplicationContext().getDatabasePath("weatherDb");
		if (database.exists()) {
			System.out.println("funnet");
			addMarkers();
		} else {
			System.out.println("ikke funnet");
		}
			

	}
	public void addMarkers(){
		 dbAdapter = new StoreWeatherAdapter(context);
		 double lat;
		 double lng;
		 String symbol;
		 LatLng point;
		 	
		    Cursor c = dbAdapter.getAllRowsPlace();
		    
		    if(c.moveToFirst()){
		    do{
		    	lat = c.getDouble(c.getColumnIndex("latitude"));
		    	lng = c.getDouble(c.getColumnIndex("longitude"));
		    	symbol  = c.getString(c.getColumnIndex("symbol"));
		    	point = new LatLng(lat, lng);
		    	symbolIcon = "img_" + symbol;
	            map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
	                               .fromResource(getResources().getIdentifier(symbolIcon,"drawable", getActivity().getPackageName()))));
		    }while(c.moveToNext());
		    dbAdapter.close();
	}else System.out.println("yaaay");
	}
	
	public void onMapClick(LatLng point) {
		clickedLat = point.latitude;
		clickedLng = point.longitude;
		jasonTask = new JsonTask(context);
		jasonTask
				.execute("http://weathermap-nith.appspot.com/locationforecast?lat="
						+ clickedLat + "&lon=" + clickedLng);
	}
	public class JsonTask extends AsyncTask<String, Void, String> {
		
		
		String symbol;
		HttpURLConnection connection = null;
		URL url;
		Context context;
		String json;
		double longitude = 0;
		double latitude = 0;
		int valueTemp;
		Weather aWeather = null;
		FragmentMap map = new FragmentMap();
		String array;
		MainActivity main;
		String date;
		String locationDate;
		ArrayList<Weather> weatherList = new ArrayList<Weather>();
		StoreWeatherAdapter dbAdapter;
		ProgressDialog dialog;
		
		public JsonTask(Context context) {
				this.context = context;
			}

			@Override
			protected String doInBackground(String... params) {
				try {
					url = new URL(params[0]);
					connection = (HttpURLConnection) url.openConnection();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					
					StringBuilder stringBuilder = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line);
					}
					json = stringBuilder.toString();
					
					return json;
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					connection.disconnect();
				}
				
				return null;
			}
			@Override
			protected void onPreExecute() {
		        dialog = new ProgressDialog(getActivity());
		        dialog.setMessage("Loading...");
		        dialog.show();
		    }
			
			@Override
			protected void onPostExecute(String result){ 
				dialog.dismiss();
				JSONObject json = null;
				dbAdapter = new StoreWeatherAdapter(context);
			
				try {
					
					json = new JSONObject(result);
					JSONObject weatherdata = json.getJSONObject("weatherdata");
					JSONObject product = weatherdata.getJSONObject("product");
					JSONArray time = product.getJSONArray("time");
					JSONObject location = null;
					
					Calendar c = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd.MM.yy");
					date = df.format(c.getTime());
					
					for (int i=0; i<time.length(); i++) {
					    JSONObject times = time.getJSONObject(i);
					    location = times.getJSONObject("location");
					    symbol = times.getString("symbol");
					    locationDate = times.getString("to");
					    latitude = location.getDouble("latitude");
					    longitude = location.getDouble("longitude");
						JSONObject temperature = location.getJSONObject("temperature");
						if(aWeather == null)
							aWeather = new Weather(latitude, longitude, date, symbol);
						valueTemp = temperature.getInt("value");
						weatherList.add(new Weather(valueTemp, locationDate ,symbol));
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				

				dbAdapter.insertPlace(aWeather);
				dbAdapter.insertWeather(weatherList);
				dbAdapter.close();
                addMarkers();
				
			}
			
		}
}
