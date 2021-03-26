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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SavedCategories extends ListActivity {

	public static final String KEY_ITEMNAME = "ItemName";
	public static final String KEY_INGREDIENTS = "Ingredients";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_RESTAURANT = "RestaurantName";
	public static final String KEY_CATEGORY = "Category";
	
	PhoneDb db = new PhoneDb(this);
	String itemInfo = "";
	String restaurantName;
	String restaurantPhoneNumber;
	String numberOfVisits;
	
	TextView tvResName,tvNumber,tvNumberOfVisits;

	ArrayList<HashMap<String, String>> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_list);
		initialize();
		getExtras();
		getAndSetRestaurantPhoneNumber();
		getAndSetRestaurantsNumberOfVisists();
		displayList();

	}
	
	private void getAndSetRestaurantsNumberOfVisists() {
		// TODO Gets and displays the number of visits
		db.open();
		numberOfVisits = db.getNumberOfVisits(restaurantName);
		db.close();
		tvNumberOfVisits.setVisibility(View.VISIBLE);
		tvNumberOfVisits.setText("Visits: "+numberOfVisits);
	}

	private void initialize() {
		// TODO link variables to the widgets and add fonts
		tvResName = (TextView) findViewById(R.id.changingText);
		tvNumber = (TextView) findViewById(R.id.tvNumber);
		tvNumberOfVisits = (TextView) findViewById(R.id.tvNumberOfVisits);
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),
				"fifawelcome.ttf");

		tvResName.setTypeface(fontfifa);
		tvNumber.setTypeface(fontfifa);
		tvNumberOfVisits.setTypeface(fontfifa);
	}
	
	private void getExtras() {
		//gets the restaurant name from the previous activity
		Intent i = getIntent();
		restaurantName = i.getStringExtra(KEY_RESTAURANT);
	}
	
	private void getAndSetRestaurantPhoneNumber() {
		// TODO Gets and displays the telephone number
		db.open();
		restaurantPhoneNumber = db.getPhoneNumber(restaurantName);
		db.close();
		tvResName.setText(restaurantName);
		tvNumber.setVisibility(View.VISIBLE);
		tvNumber.setText("Tel: " + restaurantPhoneNumber);
	}
	
	private void displayList() {
		// TODO displays te categories in a list
		db.open();
		data = db.getSavedCategories(restaurantName);
		db.close();
		Log.d("data to be listed", data.toString());
		runOnUiThread(new Runnable() {
			public void run() {
				ListAdapter adapter = new SimpleAdapter(SavedCategories.this,
						data,
						R.layout.list_element_saved_restaurants_categories,
						new String[] { KEY_CATEGORY },
						new int[] { R.id.tv_categoriesName }

				);

				setListAdapter(adapter);
			}
		});

		ListView lvc = getListView();
		lvc.setCacheColorHint(Color.TRANSPARENT);
		Log.d("SavedCategories reached here", ".........");

		lvc.setOnItemClickListener(new OnItemClickListener() {
			//setting the onItemClickListener for the ListView
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String category = ((TextView) view
						.findViewById(R.id.tv_categoriesName)).getText()
						.toString();

				Intent i = new Intent(getApplicationContext(), SavedItems.class);
				i.putExtra(KEY_CATEGORY, category);
				i.putExtra(KEY_RESTAURANT, restaurantName);

				startActivity(i);
			}
		});

		Log.d("SavedCategories reached here finalyy wilililili", ".........");
	}
}