package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ManuallyAddedItemsList extends ListActivity {
// declaring static variables
	protected static final String KEY_ITEMNAME = "ItemName";
	protected static final String KEY_INGREDIENTS ="Ingredients";
	protected static final String KEY_PRICE = "Price";
	protected static final String KEY_RESTAURANT = "RestaurantName";
	protected static final String TABLE_MANUALLYADDEDITEMS = "ManuallyAddedItems";
	
	// declaring other variables
	TextView tv_hint_ManuallyAddedItemsList, tvaddeditems;
	Button clear;
	boolean tableIsEmpty;
	
	// arraylist of hashmap to get the items from the table manuallyAddedItems
	ArrayList<HashMap<String, Object>> manuallyAddedItemsList;
	
	// initializing the phoneDB object
	PhoneDb db = new PhoneDb(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_manually_added_list);
		initialize();
		getListForAdapter();
		DisplayList();
	}

	private void initialize() {
		// TODO initialize variables and sets font
		clear = (Button)findViewById(R.id.bClearManuallyAdded);
		tv_hint_ManuallyAddedItemsList = (TextView) findViewById(R.id.tv_hint_ManuallyAddedItemsList);
		tvaddeditems = (TextView) findViewById(R.id.tvaddeditems);
		
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),
				"fifawelcome.ttf");
		clear.setTypeface(fontfifa);
		tv_hint_ManuallyAddedItemsList.setTypeface(fontfifa);
		tvaddeditems.setTypeface(fontfifa);
		
		
		// checks if the table is empty
		db.open();
		tableIsEmpty = db.isThisTableEmpty(TABLE_MANUALLYADDEDITEMS);
		db.close();
		
		if(tableIsEmpty){
			clear.setEnabled(false);
		}
		
	}
	
	private void getListForAdapter() {
		// TODO getting the list of manually added items
		db.open();
		manuallyAddedItemsList = db.getManuallyaddedItemsForAdapter();
		db.close();
	}

	private void DisplayList() {
		// TODO displaying the list
		runOnUiThread(new Runnable() {
			public void run() {

				ListAdapter adapter = new SimpleAdapter(
						ManuallyAddedItemsList.this, manuallyAddedItemsList,
						R.layout.list_element_saved_manually_added_items,
						new String[] { KEY_RESTAURANT, KEY_ITEMNAME, KEY_PRICE },
						new int[] { R.id.tv_restaurantName,R.id.tv_itemName,R.id.tv_manualPrice }

				);
				setListAdapter(adapter);
			}
		});
		 ListView lvc = getListView();
         lvc.setCacheColorHint(Color.TRANSPARENT);
         if(!manuallyAddedItemsList.isEmpty()){
         	 
       		lvc.setVisibility(View.VISIBLE);
       		tv_hint_ManuallyAddedItemsList.setVisibility(View.GONE);
         }
	}
	public void clear(View view){
		// Clear button listener that clears  the table manuallyAddedItems and refresh the activity
		db.open();
		db.clearTable(TABLE_MANUALLYADDEDITEMS);
		db.close();
		finish();
		startActivity(getIntent());
	}
}
