package com.example.weathermap;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class placeFragment extends ListFragment {
	
	  public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
	
		    ArrayList<String> places = MainActivity.placeList;
		    
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, places);
		    setListAdapter(adapter);
		  }

		  @Override
		  public void onListItemClick(ListView l, View v, int position, long id) {
		    // do something with the data

		  }

}
