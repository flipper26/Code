package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SavedItems extends ListActivity {

	public static final String KEY_ITEMNAME = "ItemName";
	public static final String KEY_INGREDIENTS = "Ingredients";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_RESTAURANT = "RestaurantName";
	public static final String KEY_CATEGORY = "Category";
	
	String restaurantName;
	String category;
	TextView tvResName, tvMustPressLiveOrderforRestaurants; 
	ArrayList<HashMap<String, String>> info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_restaurants_list);	
		getExtras();
		initialize();
		getAndDisplayItemInfo();
       
	}

	private void getExtras() {
		// TODO get the restarant's name and category from the previous application
		Intent i = getIntent();
		restaurantName = i.getStringExtra(KEY_RESTAURANT);
		category = i.getStringExtra(KEY_CATEGORY);
	}

	private void initialize() {
		// TODO initialize the variable, links them to the widgets and add font
		Typeface fontfifa = Typeface.createFromAsset(getAssets(), "fifawelcome.ttf");  
		tvResName = (TextView)findViewById(R.id.changingText);
		tvMustPressLiveOrderforRestaurants = (TextView)findViewById(R.id.tvMustPressLiveOrderforRestaurants);
		tvResName.setText(restaurantName);
		tvResName.setTypeface(fontfifa);
		tvMustPressLiveOrderforRestaurants.setTypeface(fontfifa);
	}
	
	private void getAndDisplayItemInfo() {
		// TODO gets the item's information from the table items and displays them in the list view
		PhoneDb db = new PhoneDb(SavedItems.this);
		info = new ArrayList<HashMap<String, String>>();
		db = new PhoneDb(SavedItems.this);
		db.open();
		info = db.getItemsInfo(category, restaurantName);
		db.close();
		Log.d("what the getItem info is returning:",info.toString());
		runOnUiThread(new Runnable() {
			public void run() {

				ListAdapter adapter = new SimpleAdapter(
						SavedItems.this, info,
						R.layout.list_element_saved_restaurants_items,
						new String[] { KEY_ITEMNAME, KEY_INGREDIENTS, KEY_PRICE },
						new int[] { R.id.tv_ItemName,R.id.tv_ingredients,R.id.tv_price }

				);
				setListAdapter(adapter);
			}
		});
		 ListView lvc = getListView();
         lvc.setCacheColorHint(Color.TRANSPARENT);
         if(!info.isEmpty()){
         	 
       		lvc.setVisibility(View.VISIBLE);
       		tvMustPressLiveOrderforRestaurants.setVisibility(View.GONE);
         }
         lvc.setClickable(false);
	}
}
