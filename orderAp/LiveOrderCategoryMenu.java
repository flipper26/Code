package com.application.orderAp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LiveOrderCategoryMenu extends ListActivity {

	boolean didItWorkC = true;
	boolean didItWorkR = true;
	TextView backgroundText;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	JSONObject CjObj, RjObj;
	PhoneDb db = new PhoneDb(LiveOrderCategoryMenu.this);

	ArrayList<HashMap<String, String>> categoryList;

	// url to get all categories
	private static String url_Categories = "http://192.168.1.2/OrderAp_php/get_categories.php";
	// url to get all items
	private static final String URL_GET_ALL_ITEMS = "http://192.168.1.2/OrderAp_php/get_all_items.php";
	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ITEMS = "items";
	private static final String TAG_ITEMNAME = "ItemName";
	private static final String TAG_PRICE = "Price";
	private static final String TAG_INGREDIENTS = "Ingredients";
	private static final String TAG_CATEGORY = "Category";
	private static final String TAG_RESTAURANTNAME = "RestaurantName";
	private static final String TAG_CATEGORIES = "Categories";
	private static final String TABLE_ITEMS = "items";
	private static final String TAG_RESTAURANTPHONENUMBER = "RestaurantPhoneNumber";
	

	private String todayDate;
	String restaurantName;
	String restaurantPhoneNumber;
	
	JSONArray categories = null;
	JSONArray items = null;
	
	Toast upToDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_list);
		//Siewet the content view
		backgroundText = (TextView) findViewById(R.id.changingText);
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),
				"fifawelcome.ttf");
		backgroundText.setTypeface(fontfifa);
		
		upToDate = Toast.makeText(getApplicationContext(),"Menu is up to date",Toast.LENGTH_LONG);
		
		//Getting today's date to be compared with the saved date
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

		todayDate = (day + "/" + (month + 1) + "/" + year);
		
		//loading the categories on the screen using a simple list adapter
		new LoadCategories().execute();
		
		Log.d("LiveOrderCategoryMenu/Date(sent to db)", todayDate);
		
		
		while(restaurantName == null){
		//	wait until the restaurantName becomes differant than null 
		//  so that it's value can be sent as an argument in the method getDate
		}
		
		String savedDate = "";
		db.open();
		savedDate = db.getDate(restaurantName);
		Log.d("LiveOrderCategoryMenu/savedDate(compared with todayDate)", savedDate);
		db.close();
		if(savedDate == "No Date"){
			//if there are no saved date we save the restaurant's item in the items table
			new SaveRestaurantsMenu().execute();
			//we save the restaurant's info in the restaurants table
			db.open();
			db.insertRestaurantNameAndAddCount(restaurantName, restaurantPhoneNumber, todayDate);
			db.close();
			
		}else if(todayDate.equals(savedDate)){
			//if both today's date and the saved date are equal show a msg saying the menu is up to date.
			upToDate.show();
		}else{
			// if the dates are differant 
			db.open();
			// clear table items
			db.clearTable(TABLE_ITEMS);
			//update table restaurants
			db.updateNumberOfVisitsAndDate(restaurantName, restaurantPhoneNumber, todayDate);
			db.close();
			//save the updated items
			new SaveRestaurantsMenu().execute();
		}

		// Get listview
		ListView lvc = getListView();
		lvc.setCacheColorHint(Color.TRANSPARENT);
		lvc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String category = ((TextView) view.findViewById(R.id.bCategory))
						.getText().toString();
				// Starting new intent
				Intent i = new Intent(getApplicationContext(),
						LiveOrderItemsMenu.class);
				// sending Category and restaurant name to next activity
				i.putExtra(TAG_CATEGORY, category);
				i.putExtra(TAG_RESTAURANTNAME, restaurantName);
				// starting new activity and expecting some response back
				startActivityForResult(i, 100);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 **/
	class LoadCategories extends AsyncTask<String, Integer, String> {


		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LiveOrderCategoryMenu.this);
			pDialog.setMessage("Loading items ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			
			// Hashmap for ListView
			categoryList = new ArrayList<HashMap<String, String>>();
			// Building Parameters
			List<NameValuePair> categoryParams = new ArrayList<NameValuePair>();

			Log.d ("check", "check");
			
			// getting JSON string from URL
			CjObj = jParser.makeHttpRequest(url_Categories, "GET",
					categoryParams);
		
			Log.d("LiveOrderItem/LoadCategories/doInBackground","Exeption not thrown and loadCategories resumed");
			// Check your log cat for JSON reponse
			Log.d("LiveOrderCategoryMenu/LoadCategories/CjObj(json object)", CjObj.toString());

			try {
				// Checking for SUCCESS TAG
				int success = CjObj.getInt(TAG_SUCCESS);

				if (success == 1) {
					// items found
					// Getting Array of Items
			
					restaurantName = CjObj.getString(TAG_RESTAURANTNAME);
					restaurantPhoneNumber = CjObj.getString(TAG_RESTAURANTPHONENUMBER);
					
					Log.d("LiveOrderCategoryMenu/LoadCategories/restaurantName and its phone number :", restaurantName+"Phone Number:"+restaurantPhoneNumber);
					
					categories = CjObj.getJSONArray(TAG_CATEGORIES);

					// looping through All Categories
					for (int i = 0; i < categories.length(); i++) {
						JSONObject c = categories.getJSONObject(i);
						// Storing each json item in variable;
						String category = c.getString(TAG_CATEGORY);
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();
						// adding each child node to HashMap key => value
						map.put(TAG_CATEGORY, category);
						// adding HashList to ArrayList
						categoryList.add(map);

					}
				} else {
					// no Items found
					Toast noItems = Toast.makeText(getApplicationContext(),
							"No items found!", Toast.LENGTH_LONG);
					noItems.show();
					finish();
				}

			} catch (JSONException e) {
				didItWorkC = false;
				e.printStackTrace();
			} catch (Exception e) {
				didItWorkC = false;
				e.printStackTrace();
			}
			
			Log.d("LiveOrderCategoryMenu/LoadCategories/categoryList: ", categoryList.toString());
			
			return null;
		
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all Items
			pDialog.dismiss();
			if (didItWorkC == true) {
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						ListAdapter adapter = new SimpleAdapter(
								LiveOrderCategoryMenu.this,
								categoryList,
								R.layout.list_element_live_order_category,
								new String[] { TAG_CATEGORY },
								new int[] { R.id.bCategory }

						);
						// updating listview
						setListAdapter(adapter);
					}
				});
			}else {
				Toast error = Toast.makeText(getApplicationContext(),
						"Error: failed to connect, please try again :",
						Toast.LENGTH_LONG);
				error.show();
				finish();
			}
		}
	}
	class SaveRestaurantsMenu extends AsyncTask<String, Integer, String> {

		protected void onProgressUpdate(Integer... progress) {
			pDialog.incrementProgressBy(progress[0]);
		}

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LiveOrderCategoryMenu.this);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setMax(100);
			pDialog.setMessage("Saving...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> restParams = new ArrayList<NameValuePair>();

			// getting JSON string from URL
			RjObj = jParser.makeHttpRequest(URL_GET_ALL_ITEMS, "GET",
					restParams);

			try {
				// Checking for SUCCESS TAG
				int success = RjObj.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of items
					items = RjObj.getJSONArray(TAG_ITEMS);

					// looping through All items
					for (int i = 0; i < items.length(); i++) {
						JSONObject c = items.getJSONObject(i);
						// Storing each json item in variable
						String itemName = c.getString(TAG_ITEMNAME);
						String ingredients = c.getString(TAG_INGREDIENTS);
						String category = c.getString(TAG_CATEGORY);
						int price = c.getInt(TAG_PRICE);
						Log.d("LiveOrderCategoryMenu/SaveRestaurantsMenu/Got Strings in variables) ", "===============");
						
						//save the items gotten to the table items using the method saveRowToDb
						saveRowToDb(itemName, ingredients, category, price,
								restaurantName);
						

						int incrementBy = items.length() / 100;
						publishProgress(incrementBy);

					}

				}
			} catch (JSONException e) {
				didItWorkR = false;
				e.printStackTrace();
			} catch (Exception e) {
				didItWorkR = false;
				e.printStackTrace();
			}

			Log.d("LiveOrderCategoryMenu/SaveRestaurantsMenu/RjObj(json object) ", RjObj.toString());
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all Items
			pDialog.dismiss();
			if (didItWorkR) {
				upToDate.show();
			} else {
				Toast notSaved = Toast.makeText(getApplicationContext(),
						"Unable to save", Toast.LENGTH_LONG);
				notSaved.show();
			}

		}
	}

	private void saveRowToDb(String itemName, String ingredients,
			String category, int price, String restaurantName) {
		// TODO Auto-generated method stub
		//used to save items to the phone's database table items
		try {
			db.open();
			db.itemsTableEntry(itemName, ingredients, category, price,
					restaurantName);
			db.close();
			Log.d("LiveOrderCategoryMenu/saveRowToDb/finished", "!!!!!!!");
		} catch (Exception e) {
			didItWorkR = false;
		}
	}
	
public void ShowDialog(){
		
		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
		
			popDialog.setTitle("Try Again?");
			popDialog.setMessage("Order AP is trying to connect you to a restaurant but has encountered a problem, make sure you are in a restaurant's range. Would you like to try again?");
		

		// Button OK
		popDialog.setPositiveButton("Yes, please",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						
						dialog.dismiss();
						new LoadCategories().execute();
					}

				})

		// Button Cancel
				.setNegativeButton("No, thanks",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								finish();
								Intent menuStarter = new Intent(LiveOrderCategoryMenu.this, OrderApMenu.class);
								startActivity(menuStarter);
							}
						});

		popDialog.create();
		popDialog.show();
        
	}
}