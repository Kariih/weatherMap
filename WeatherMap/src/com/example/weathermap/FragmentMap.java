package com.example.weathermap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FragmentMap extends MapFragment {

	GoogleMap map;
	double clickedLat;
	double clickedLng;
	JsonTask jasonTask;
	String pictureSymbolName;
	String resorce;
	Context context;
	String symbolIcon;
	public static String markerMap;

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		context = getActivity().getApplicationContext();

		map = getMap();
		map.setMyLocationEnabled(true);
		
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				clickedLat = point.latitude;
				clickedLng = point.longitude;
				jasonTask = new JsonTask(context);
				jasonTask
						.execute("http://weathermap-nith.appspot.com/locationforecast?lat="
								+ clickedLat + "&lon=" + clickedLng);
			}	
		});

	}
	public void addMarker(LatLng point) {
		symbolIcon = "img_" + jasonTask.symbol;
		Marker marker = map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
				   .fromResource(getResources().getIdentifier(symbolIcon,"drawable", getActivity().getPackageName()))));
	}
	public void addMarker(LatLng point, String symbolIcon2) {
		symbolIcon2 = "img_" + jasonTask.symbol;
		Marker marker = map.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory
				   .fromResource(getResources().getIdentifier(symbolIcon,"drawable", getActivity().getPackageName()))));
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
		Weather aWeather;
		FragmentMap map = new FragmentMap();
		String array;
		MainActivity main;
		String date;
		
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
			protected void onPostExecute(String result) {
				JSONObject json = null;
			
				try {
					
					json = new JSONObject(result);
					JSONObject weatherdata = json.getJSONObject("weatherdata");
					JSONObject product = weatherdata.getJSONObject("product");
					JSONArray time = product.getJSONArray("time");
					JSONObject location = null;
					
					
					for (int i=0; i<time.length(); i++) {
					    JSONObject times = time.getJSONObject(i);
					    location = times.getJSONObject("location");
					    symbol = times.getString("symbol");
					    latitude = location.getDouble("latitude");
					    longitude = location.getDouble("longitude");
					    String array = times.toString();
						JSONObject temperature = location.getJSONObject("temperature");
						valueTemp = temperature.getInt("value");
					}
					LatLng point = new LatLng(latitude, longitude);
					addMarker(point);
					
					Calendar c = Calendar.getInstance();
					SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd.MM.yy");
					date = df.format(c.getTime());
					aWeather = new Weather(latitude, longitude, date, symbol);
					MainActivity.placeList.add(aWeather.toString());
					MainActivity.markerList.add(aWeather);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
			}

		}
}
