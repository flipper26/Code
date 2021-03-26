package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SavedRestaurantNames extends ListActivity {

	public static final String KEY_RESTAURANT = "RestaurantName";
	private static final String TONEXTACTIVITY = "toNextActivity";
	public static final String KEY_DATE = "Date";
	TextView tvRestaurants, rm_restaurantName, rm_LastSavedDate, tvMustPressLiveOrderforRestaurants;
	ListView lvc;
	Typeface fontfifa;
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.layout_restaurants_list);
	        initialize();
	        getExtras();
	        getAndDisplayItemInfo();
            
	  }
	
	private void getExtras() {
		// TODO gets a string from the previous activity
		Intent i = getIntent();
        String restaurants = i.getStringExtra(TONEXTACTIVITY);
        tvRestaurants.setText(restaurants);	
	}
	private void initialize() {
		// TODO initialize the variable, links them to the widgets and add font
		tvRestaurants = (TextView)findViewById(R.id.changingText);
        tvMustPressLiveOrderforRestaurants = (TextView)findViewById(R.id.tvMustPressLiveOrderforRestaurants);
        fontfifa = Typeface.createFromAsset(getAssets(), "fifawelcome.ttf");
    	tvRestaurants.setTypeface(fontfifa);
        tvMustPressLiveOrderforRestaurants.setTypeface(fontfifa);
        
	}

	private void getAndDisplayItemInfo() {
		// TODO gets the item's information from the table items and displays them in the list view
	      PhoneDb db = new PhoneDb(this);
	        db.open();
	        final ArrayList<HashMap<String, String>> data = db.getSavedRestaurantName();
	        db.close();
	    	
	        runOnUiThread(new Runnable() {
              public void run() {

               ListAdapter adapter = new SimpleAdapter(
              		 SavedRestaurantNames.this,
                  		data,
                  		R.layout.list_element_saved_restaurants_names, 
                  		new String[] {KEY_RESTAURANT, KEY_DATE},
                  		new int[] { R.id.rm_restaurantName,R.id.rm_LastSavedDate}
              
                  		);
                  setListAdapter(adapter);
              }
          });
	     
	        lvc = getListView();
	        lvc.setCacheColorHint(Color.TRANSPARENT);
	        
	   	 	if(!data.isEmpty()){
       	 
    		lvc.setVisibility(View.VISIBLE);
    		tvMustPressLiveOrderforRestaurants.setVisibility(View.GONE);
    		}
	   	 
	        lvc.setOnItemClickListener(new OnItemClickListener() {
	 
	        	 //setting the OnItemClikListener
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	                String restaurantName = ((TextView)view.findViewById(R.id.rm_restaurantName)).getText()
	                        .toString();
	                Intent i = new Intent(getApplicationContext(),
	                        SavedCategories.class);
	                i.putExtra(KEY_RESTAURANT, restaurantName);
	                startActivity(i);
	            }
	        });
	}
}