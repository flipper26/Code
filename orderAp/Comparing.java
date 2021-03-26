package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Comparing extends Activity {

	private final static String TAG_FIRST_RESTAURANT = "firstRestaurantName";
	private final static String TAG_SECOND_RESTAURANT = "secondRestaurantName";
	private final static String TAG_FIRST_ITEMNAME = "firstItemName";
	private final static String TAG_SECOND_ITEMNAME = "secondItemName";

	// declaring varibles
	EditText edFirstRestName, edFirstItemName, edSecondRestName,
			edSecondItemName;
	Button bSubmit, bClear;
	
	TextView tvComparingMessage,tvFirstItem,tvSecondItem,tvComparingChangingText;
	
	InputMethodManager inputManager;

	private ListView lvRest, lvItems, lvRest2, lvItems2;

	String[] itemsArray;
	String[] itemsArray2;
	String[] restaurantsArray;

	String SelectedRestaurant = "";
	String SelectedRestaurant2 = "";
	String SelectedItem = "";

	ArrayAdapter<String> adapter, adapter2;

	ArrayList<HashMap<String, String>> productList;

//	Creating an instance of the database helper class
	PhoneDb db = new PhoneDb(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//set the layout
		setContentView(R.layout.layout_comparing);
		checkScreenDensity();
		findIdsAndSetFont();
		initialize();
		SetupEditText();
	}

	public void checkScreenDensity() {
		//Check screen density using a switch case to include or exclude the orderAP logo
		RelativeLayout rlIconComparing = (RelativeLayout) findViewById(R.id.rlIconComparing);
		ImageView ivHlineCopmaring= (ImageView) findViewById(R.id.ivHlineCopmaring);
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			rlIconComparing.setVisibility(View.GONE);
			ivHlineCopmaring.setVisibility(View.GONE);
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			rlIconComparing.setVisibility(View.GONE);
			ivHlineCopmaring.setVisibility(View.GONE);
			break;
		case DisplayMetrics.DENSITY_HIGH:
			rlIconComparing.setVisibility(View.GONE);
			ivHlineCopmaring.setVisibility(View.GONE);
			break;
		case DisplayMetrics.DENSITY_TV:
			rlIconComparing.setVisibility(View.VISIBLE);
			ivHlineCopmaring.setVisibility(View.VISIBLE);
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			rlIconComparing.setVisibility(View.VISIBLE);
			ivHlineCopmaring.setVisibility(View.VISIBLE);
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			rlIconComparing.setVisibility(View.VISIBLE);
			ivHlineCopmaring.setVisibility(View.VISIBLE);
			break;
		}
	}
	private void findIdsAndSetFont() {
		// TODO Auto-generated method stub
		// connect java variables with the XML widgets usning the ID
		// add font to textviews and buttons
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),"fifawelcome.ttf");
		edFirstRestName = (EditText) findViewById(R.id.edFirstRestName);
		edFirstItemName = (EditText) findViewById(R.id.edFirstItemName);
		edSecondRestName = (EditText) findViewById(R.id.edSecondRestName);
		edSecondItemName = (EditText) findViewById(R.id.edSecondItemName);
		lvRest = (ListView) findViewById(R.id.list_view_restaurants);
		lvItems = (ListView) findViewById(R.id.list_view_items);
		lvRest2 = (ListView) findViewById(R.id.list_view_secondRestaurants);
		lvItems2 = (ListView) findViewById(R.id.list_view_second_items);
		bSubmit = (Button) findViewById(R.id.bsubmit);
		bClear = (Button) findViewById(R.id.bclear);
		tvComparingMessage = (TextView) findViewById(R.id.tvComparingMessage);
		tvFirstItem = (TextView) findViewById(R.id.tvFirstItem);
		tvSecondItem = (TextView) findViewById(R.id.tvSecondItem);
		tvComparingChangingText = (TextView) findViewById(R.id.tvComparingChangingText);
		
		bSubmit.setTypeface(fontfifa);
		bClear.setTypeface(fontfifa);
		tvComparingMessage.setTypeface(fontfifa);
		tvFirstItem.setTypeface(fontfifa);
		tvSecondItem.setTypeface(fontfifa);
		tvComparingChangingText.setTypeface(fontfifa);
	}

	private void initialize() {

		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//Getting a list of the restaurants saved on the divice in an array
		db.open();
		restaurantsArray = db.getListOfRestaurants();
		db.close();
		//creating 2 adapters using the restaurants array we got above
		adapter = new ArrayAdapter<String>(this,
				R.layout.list_element_manual_adding, R.id.ManualAddingListTv,
				restaurantsArray);
		adapter2 = new ArrayAdapter<String>(this,
				R.layout.list_element_manual_adding, R.id.ManualAddingListTv,
				restaurantsArray);
		//setting the adapters of the listviews for both choices of restaurant and item
		lvRest.setAdapter(adapter);
		lvItems.setAdapter(adapter);
		lvRest2.setAdapter(adapter2);
		lvItems2.setAdapter(adapter2);

		lvRest.setVisibility(View.INVISIBLE);
		lvItems.setVisibility(View.GONE);
		lvRest2.setVisibility(View.INVISIBLE);
		lvItems2.setVisibility(View.GONE);

		edSecondItemName.setVisibility(View.GONE);
		edFirstItemName.setVisibility(View.GONE);
		//Setting the on clickListener on the listviews
		lvRest.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SelectedRestaurant = ((TextView) view
						.findViewById(R.id.ManualAddingListTv)).getText()
						.toString();
				edFirstRestName.setText(SelectedRestaurant);
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				lvRest.setVisibility(View.INVISIBLE);
				
			}

		});

		lvRest2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SelectedRestaurant = ((TextView) view
						.findViewById(R.id.ManualAddingListTv)).getText()
						.toString();
				edSecondRestName.setText(SelectedRestaurant);
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				lvRest2.setVisibility(View.INVISIBLE);
			}

		});

		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SelectedItem = ((TextView) view
						.findViewById(R.id.ManualAddingListTv)).getText()
						.toString();
				edFirstItemName.setText(SelectedItem);
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				lvItems.setVisibility(View.INVISIBLE);

			}

		});

		lvItems2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SelectedItem = ((TextView) view
						.findViewById(R.id.ManualAddingListTv)).getText()
						.toString();
				edSecondItemName.setText(SelectedItem);
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				lvItems2.setVisibility(View.INVISIBLE);
			}

		});
	}
//Setting up the edit text by setting an onTextChangedListener and giving it the adapters
	public void SetupEditText() {

		edFirstRestName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				Comparing.this.adapter.getFilter().filter(cs);
				lvRest.setVisibility(View.VISIBLE);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (edFirstRestName.getText().toString().equals("")) {
					lvRest.setVisibility(View.INVISIBLE);
				}
			}
		});

		edSecondRestName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				Comparing.this.adapter2.getFilter().filter(cs);
				lvRest2.setVisibility(View.VISIBLE);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (edSecondRestName.getText().toString().equals("")) {
					lvRest2.setVisibility(View.INVISIBLE);
				}
			}
		});

		edFirstItemName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				Comparing.this.adapter.getFilter().filter(cs);
				lvItems.setVisibility(View.VISIBLE);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (edFirstItemName.getText().toString().equals("")) {
					lvItems.setVisibility(View.INVISIBLE);
				}
			}
		});

		edSecondItemName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				Comparing.this.adapter2.getFilter().filter(cs);
				lvItems2.setVisibility(View.VISIBLE);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (edSecondItemName.getText().toString().equals("")) {
					lvItems2.setVisibility(View.INVISIBLE);
				}
			}
		});

	}
	//method that checks if the string array in the second arguments contains the string that is the first argument
	public boolean contains(String someString, String[] someStringArray) {
		boolean contains = false;
		for(int i = 0; i < someStringArray.length; i++){
			if(someStringArray[i].equals(someString)){
				contains = true;
			}
		}
		
		return contains;
	}
	
// what to when the button next/compare is clicked
	public void submit(View view) {

		Intent in = new Intent(getApplicationContext(),
				ComparingResult.class);
		
		if (bSubmit.getText().toString().equals("Next")) {
			//if the button is next make sure that both restaurant feilds are filled
			if ((edFirstRestName.getText().toString().equals(""))
					|| (edSecondRestName.getText().toString().equals(""))) {
				Toast noRestaurant = Toast.makeText(getApplicationContext(),
						"Please select two restaurants ", Toast.LENGTH_LONG);
				noRestaurant.show();
			} else if(contains(edFirstRestName.getText().toString(),restaurantsArray) && contains(edSecondRestName.getText().toString(),restaurantsArray)){
				//make sure the selection is in the list, and get the text
				SelectedRestaurant = edFirstRestName.getText().toString();
				SelectedRestaurant2 = edSecondRestName.getText().toString();
				//use the gotten text to get 2 new arrays (itemArray and itemArray2) 
				db.open();
				itemsArray = db.getListOfItems(SelectedRestaurant);
				itemsArray2 = db.getListOfItems(SelectedRestaurant2);
				db.close();
				Log.d("Comparing/lvRest.setOnItemClickListener",
						itemsArray.toString());

				//Clear the adapters and fill them with the arrays gotten above
				adapter.clear();
				int count = 0;
				for (String x : itemsArray) {
					adapter.insert(x, count);
					count++;
				}
				adapter.notifyDataSetChanged();
				edFirstItemName.setVisibility(View.VISIBLE);
				lvRest.setVisibility(View.GONE);

				adapter2.clear();
				int count2 = 0;
				for (String x : itemsArray2) {
					adapter2.insert(x, count2);
					count2++;
				}
				adapter2.notifyDataSetChanged();
				edSecondItemName.setVisibility(View.VISIBLE);
				lvRest2.setVisibility(View.GONE);
				bClear.setVisibility(View.VISIBLE);
				edFirstRestName.setEnabled(false);
				edSecondRestName.setEnabled(false);
				bSubmit.setText("Compare");
				tvComparingMessage.setText("Now select both items you wish to compare and press Compare");
			}
		} else if (bSubmit.getText().toString().equals("Compare")) {
			//if the button is compare check that the edit texts are filled 
			if ((edFirstItemName.getText().toString().equals(""))
					|| (edSecondItemName.getText().toString().equals(""))) {
				Toast noItem = Toast.makeText(getApplicationContext(),
						"Please select two items to compare ",
						Toast.LENGTH_LONG);
				noItem.show();
			} else if(contains(edFirstItemName.getText().toString(),itemsArray) && contains(edSecondItemName.getText().toString(),itemsArray2)) {
				//check that the selected items are in the list 
				String firstRestaurantName = edFirstRestName.getText().toString();
				String firstItemName = edFirstItemName.getText().toString();
				String secondRestaurantName = edSecondRestName.getText().toString();
				String secondItemName = edSecondItemName.getText().toString();
				//add thevalues from the 4 feilds as extras in the intent that will start the next activity
				in.putExtra(TAG_FIRST_RESTAURANT, firstRestaurantName);
				in.putExtra(TAG_FIRST_ITEMNAME, firstItemName);
				in.putExtra(TAG_SECOND_RESTAURANT, secondRestaurantName);
				in.putExtra(TAG_SECOND_ITEMNAME, secondItemName);
				//start the next activity
				startActivity(in);
			}else{
				Toast noValidItems = Toast.makeText(getApplicationContext(),
						"Invalid item name", Toast.LENGTH_LONG);
				noValidItems.show();
			}
		}
	}
	//clear botton will call initialize setupEditTexts end empty the editexts change the button's text to "next"
	public void clear(View view) {
		adapter.clear();
		adapter2.clear();
		initialize();
		SetupEditText();
		edFirstRestName.setText("");
		edFirstItemName.setText("");
		edSecondRestName.setText("");
		edSecondItemName.setText("");
		bSubmit.setText("Next");
		bClear.setVisibility(View.INVISIBLE);
		edFirstRestName.setEnabled(true);
		edSecondRestName.setEnabled(true);
		edFirstRestName.requestFocus();

	}

}
