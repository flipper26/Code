package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LiveOrderItemsMenu extends ListActivity {

	boolean didItWork = true;
	TextView backgroundText;

	// string value of the pressed category and restaurant name from previous activity
	String category, restaurantName, pictureUrl;

	// category items JSONArray
	JSONArray categoryItems = null;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// List of category item
	ArrayList<HashMap<String, Object>> categoryItemsList;
	// URL to get the category items
	private static final String url_post_category_items = "http://192.168.1.2/OrderAp_php/get_category_items.php?Category=category";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRICE = "Price";
	private static final String TAG_ITEMNAME = "ItemName";
	private static final String TAG_INGREDIENTS = "Ingredients";
	private static final String TAG_CATEGORY = "Category";
	private static final String TAG_CATEGORYITEMS = "categoryItems";
	private static final String TAG_RESTAURANTNAME = "RestaurantName";
	private static final String TAG_RATINGSUM = "RatingSum";
	private static final String TAG_RATINGNUM = "RatingNum";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_list);

		// Setting the font
		backgroundText = (TextView) findViewById(R.id.changingText);
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),
				"fifawelcome.ttf");
		backgroundText.setTypeface(fontfifa);

		// Hashmap for ListView
		categoryItemsList = new ArrayList<HashMap<String, Object>>();
		
		// getting string category , selected from previous intent
		Intent i = getIntent();
		category = i.getStringExtra(TAG_CATEGORY);
		restaurantName = i.getStringExtra(TAG_RESTAURANTNAME);
		pictureUrl = null;
		
		// Getting Category Items in background thread
		new GetSelectedCategoryItems().execute();

		// Get listview

		ListView lvc = getListView();
		lvc.setCacheColorHint(Color.TRANSPARENT);
		lvc.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String itemName = ((TextView) view
						.findViewById(R.id.ci_itemName)).getText().toString();
				String price = ((TextView) view.findViewById(R.id.ci_price))
						.getText().toString();
				String ingredients = ((TextView) view
						.findViewById(R.id.ci_ingredients)).getText()
						.toString();
				String category = ((TextView) view
						.findViewById(R.id.ci_category)).getText().toString();
				String ratingSum = ((TextView) view
						.findViewById(R.id.ci_ratingSum)).getText().toString();
				String ratingNum = ((TextView) view
						.findViewById(R.id.ci_ratingNum)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						LiveOrderItem.class);
				// sending ItemName, Price, Ingredients, Category, RestaurantName, ratingSum and ratingNum to next activity
				in.putExtra(TAG_ITEMNAME, itemName);
				in.putExtra(TAG_PRICE, price);
				in.putExtra(TAG_INGREDIENTS, ingredients);
				in.putExtra(TAG_CATEGORY, category);
				in.putExtra(TAG_RESTAURANTNAME, restaurantName);
				in.putExtra(TAG_RATINGSUM, ratingSum);
				in.putExtra(TAG_RATINGNUM, ratingNum);
				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
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
	 * Background Async Task to Save product Details
	 * */
	class GetSelectedCategoryItems extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LiveOrderItemsMenu.this);
			pDialog.setIndeterminate(false);
			pDialog.setMessage("Loading your Items ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_CATEGORY, category));

			Log.d("category in the list ", params.toString());

			// sending modified data through http request

			JSONObject json = jsonParser.makeHttpRequest(
					url_post_category_items, "POST", params);

			Log.d("category sent to php ", json.toString());

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully updated
					categoryItems = json.getJSONArray(TAG_CATEGORYITEMS);

					for (int i = 0; i < categoryItems.length(); i++) {
						JSONObject c = categoryItems.getJSONObject(i);

						// Storing each json item in variable
						String itemName = c.getString(TAG_ITEMNAME);
						int price = c.getInt(TAG_PRICE);
						String ingredients = c.getString(TAG_INGREDIENTS);
						String ratingSum = c.getString(TAG_RATINGSUM);
						String ratingNum = c.getString(TAG_RATINGNUM);
						Log.d("category items stored ", c.toString());
						// creating new HashMap
						HashMap<String, Object> map = new HashMap<String, Object>();

						// adding each child node to HashMap key => value
						map.put(TAG_ITEMNAME, itemName);
						map.put(TAG_INGREDIENTS, ingredients);
						map.put(TAG_PRICE, price + " L.L.");
						map.put(TAG_CATEGORY, category);
						map.put(TAG_RATINGSUM, ratingSum);
						map.put(TAG_RATINGNUM, ratingNum);

						// adding HashList to ArrayList
						categoryItemsList.add(map);
						Log.d("category items in the map ", map.toString());
					}
				} else {
					Toast error = Toast.makeText(getApplicationContext(),
							"Error: No items found ", Toast.LENGTH_LONG);
					error.show();
				}
			} catch (JSONException e) {
				didItWork = false;
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once items are loaded
			pDialog.dismiss();
			if (didItWork == false) {
				Toast error = Toast.makeText(getApplicationContext(),
						"Error: failed to connect, please try again :/",
						Toast.LENGTH_LONG);
				error.show();

			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed JSON data into ListView
						 * */
						ListAdapter adapter = new SimpleAdapter(
								LiveOrderItemsMenu.this, categoryItemsList,
								R.layout.list_element_live_order_items,

								new String[] { TAG_ITEMNAME, TAG_PRICE,
										TAG_INGREDIENTS, TAG_CATEGORY,TAG_RATINGSUM , TAG_RATINGNUM },
								new int[] { R.id.ci_itemName, R.id.ci_price,
										R.id.ci_ingredients, R.id.ci_category, R.id.ci_ratingSum, R.id.ci_ratingNum });
						// updating listview
						setListAdapter(adapter);
					}
				});
			}
		}
	}
}