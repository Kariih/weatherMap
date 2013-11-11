package com.example.weathermap;

import java.util.ArrayList;
import java.util.List;

public class Weather {

	public List<Weather> weatherArray = new ArrayList<Weather>();
	private String weatherString;
	private int temperature;
	private String symbol;
	private double latitude;
	private double lonitude;
	private String date;

	public Weather() {
	}

	public Weather(double latitude, double longitude, String date) {
		setLat(latitude);
		setLon(longitude);
		setDate(date);
	}
	public Weather(double latitude, double longitude, String date, String symbol) {
		setLat(latitude);
		setLon(longitude);
		setDate(date);
		setWeatherType(symbol);
	}
	public Weather(int temperature, String date, String symbol) {
		setTemperature(temperature);
		setDate(date);
		setWeatherType(symbol);
	}
	
	public String getWeatherString() {
		return weatherString;
	}

	public void setWeatherString(String weatherString) {
		this.weatherString = weatherString;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String time) {
		this.date = time;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public String getWeatherType() {
		return symbol;
	}

	public void setWeatherType(String symbol) {
		this.symbol = symbol;
	}

	public List<Weather> getWeatherArray() {
		return weatherArray;
	}

	public void setWeatherArray(List<Weather> weatherArray) {
		this.weatherArray = weatherArray;
	}

	public double getLat() {
		return latitude;
	}

	public void setLat(double lat) {
		this.latitude = lat;
	}

	public double getLon() {
		return lonitude;
	}

	public void setLon(double lon) {
		this.lonitude = lon;
	}
	public String toString(){
		return "Lat: " + latitude + " Lng: " + lonitude + "\nDate: " + date;
	}
}
