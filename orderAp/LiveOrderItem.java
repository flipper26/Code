package com.application.orderAp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LiveOrderItem extends Activity {

	// declaring variables ****************************************************************************************************************
	boolean didItWork = true;

	ArrayList<HashMap<String, String>> categoryList;
	ArrayList<String> withList = new ArrayList<String>();
	ArrayList<String> noList = new ArrayList<String>();
	ArrayList<String> addOnList = new ArrayList<String>();

	String listStringNo = "";
	String listStringWith = "";
	String listStringAddon = "";
	String no, with, addon;

	InputMethodManager inputManager;

	private static final String TAG_ITEMNAME = "ItemName";
	private static final String TAG_PRICE = "Price";
	private static final String TAG_INGREDIENTS = "Ingredients";
	private static final String TAG_CATEGORY = "Category";
	private static final String TAG_RESTAURANTNAME = "RestaurantName";
	private static final String TAG_PICTURE = "Picture";
	private static final String TAG_URLARRAY = "urlArray";
	private static final String TABLE_HISTORY = "History";
	private static final String TAG_RATINGSUM = "RatingSum";
	private static final String TAG_RATINGNUM = "RatingNum";

	private static final String url_post_itemName = "http://192.168.1.2/OrderAp_php/get_picture_url.php?ItemName=itemName";

	private String Date;

	TextView tvItemName, tvIngredientsFromDb, tvPriceFromDb, tvModify, tvWith,
			tvNo, tvAddOn, tvNumOfRatings;

	ImageView ivPicture, ivLine;

	EditText etAdditionalNotes;

	Button bAddToOrder, bQuantityPlus, bQuantityMinus, bQuantity,
			bClearModifications, clearWith, clearNo, clearAddOn,
			bLiveOrderHistory;

	RatingBar rbRecievedUserRating;

	String itemName, price, ingredient, category, restaurantName,
			withSelectedValue, noSelectedValue, addSelectedValue, ratingSum,
			ratingNum, pictureUrl = null;

	Spinner spinnerWith, spinnerNo, spinnerAddon;

	PhoneDb PDB;

	Bitmap myImageBmp;

	JSONParser jsonParser = new JSONParser();
	JSONArray urlArray = null;
	ArrayList<HashMap<String, Object>> categoryItemsList;

	String[] arrayWith = {};
	String[] arrayNo = {};
	String[] arrayAddon = {};

	int quantity = 1;
	int addOnPrice = 0;
	float intRatingSum, intRatingNum;

	Toast warning;
//*****************************************************************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_live_order_item);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		getExtras();
		initialize();
		setRating();
		checkHistoryTable();
		new GetPictureUrl().execute();
		GetArrayLists();
		SetSpinnersOnItemSelectedListener();

		tvItemName.setText(itemName);
		tvPriceFromDb.setText(price);
		tvIngredientsFromDb.setText(ingredient);
		bQuantity.setText("Quantity: " + quantity);

	}

	private void checkHistoryTable() {
		// TODO Auto-generated method stub
		//checks if the item has been ordered by cheking if it's in table history
		PDB.open();
		boolean empty = PDB.isThisRowEmpty(TABLE_HISTORY, itemName);
		PDB.close();

		if (empty) {
			//if empty the button history is hidden
			bLiveOrderHistory.setVisibility(View.GONE);
		}
	}

	private void getExtras() {
		//get data sent from the previous activity from the intent
		Intent i = getIntent();
		itemName = i.getStringExtra(TAG_ITEMNAME);
		price = i.getStringExtra(TAG_PRICE);
		ingredient = i.getStringExtra(TAG_INGREDIENTS);
		category = i.getStringExtra(TAG_CATEGORY);
		restaurantName = i.getStringExtra(TAG_RESTAURANTNAME);
		ratingSum = i.getStringExtra(TAG_RATINGSUM);
		ratingNum = i.getStringExtra(TAG_RATINGNUM);

		intRatingSum = Integer.parseInt(ratingSum);
		intRatingNum = Integer.parseInt(ratingNum);

	}

	private void initialize() {
		//link varables to the widgets and add fonts
		tvWith = (TextView) findViewById(R.id.tvWith);
		tvNo = (TextView) findViewById(R.id.tvNo);
		tvAddOn = (TextView) findViewById(R.id.tvAddOn);
		tvItemName = (TextView) findViewById(R.id.tvItemName);
		tvIngredientsFromDb = (TextView) findViewById(R.id.tvingredientsFromDB);
		tvPriceFromDb = (TextView) findViewById(R.id.tvpriceFromDb);
		ivPicture = (ImageView) findViewById(R.id.ivPicture);
		ivLine = (ImageView) findViewById(R.id.ivLine);
		bAddToOrder = (Button) findViewById(R.id.bAddToOrder);
		bQuantityPlus = (Button) findViewById(R.id.bQuantityPlus);
		bQuantityMinus = (Button) findViewById(R.id.bQuantityMinus);
		bQuantity = (Button) findViewById(R.id.bQuantity);
		bClearModifications = (Button) findViewById(R.id.bClearModifications);
		clearWith = (Button) findViewById(R.id.clearWith);
		clearNo = (Button) findViewById(R.id.clearNo);
		clearAddOn = (Button) findViewById(R.id.clearAddOn);
		bLiveOrderHistory = (Button) findViewById(R.id.bLiveOrderHistory);
		spinnerWith = (Spinner) findViewById(R.id.spinner_with);
		spinnerNo = (Spinner) findViewById(R.id.spinner_no);
		spinnerAddon = (Spinner) findViewById(R.id.spinner_add);
		etAdditionalNotes = (EditText) findViewById(R.id.etAdditionalNotes);
		rbRecievedUserRating = (RatingBar) findViewById(R.id.ratingBarRecivedUserRating);
		tvNumOfRatings = (TextView) findViewById(R.id.tvNumOfRatings);

		Typeface fontfifa = Typeface.createFromAsset(getAssets(),
				"fifawelcome.ttf");
		tvWith.setTypeface(fontfifa);
		tvNo.setTypeface(fontfifa);
		tvAddOn.setTypeface(fontfifa);
		tvItemName.setTypeface(fontfifa);
		tvIngredientsFromDb.setTypeface(fontfifa);
		tvPriceFromDb.setTypeface(fontfifa);
		bAddToOrder.setTypeface(fontfifa);
		bQuantityPlus.setTypeface(fontfifa);
		bQuantityMinus.setTypeface(fontfifa);
		bQuantity.setTypeface(fontfifa);
		bClearModifications.setTypeface(fontfifa);
		clearWith.setTypeface(fontfifa);
		clearNo.setTypeface(fontfifa);
		clearAddOn.setTypeface(fontfifa);
		bLiveOrderHistory.setTypeface(fontfifa);
		tvNumOfRatings.setTypeface(fontfifa);

		if (ingredient.equals("")) {
			tvIngredientsFromDb.setVisibility(View.GONE);
		}

		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		PDB = new PhoneDb(LiveOrderItem.this);

		ivPicture.requestFocus();
	}

	private void setRating() {
		// TODO Auto-generated method stub
		//set the text below the rating bar according to the number of ratings.
		if (intRatingNum == 0) {
			tvNumOfRatings.setText("Not Rated yet.");
		} else {
			float RatingToDisplay = intRatingSum / intRatingNum;
			Log.d("LiveOrderItem/ Rating to display/ratingSum/ratingNumber:",String.valueOf(RatingToDisplay)+"/"+ String.valueOf(intRatingSum)+"/"+String.valueOf(intRatingNum));
			if (intRatingNum == 1) {
				rbRecievedUserRating.setRating(RatingToDisplay);
				tvNumOfRatings.setText("Rated once");
			} else {
				rbRecievedUserRating.setRating(RatingToDisplay);
				tvNumOfRatings.setText("Rated " + ratingNum + " times");
			}
		}
	}

	class GetPictureUrl extends AsyncTask<String, String, String> {
		// get the picture's url from the server's database
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair(TAG_ITEMNAME, itemName));
			JSONObject json = jsonParser.makeHttpRequest(url_post_itemName,
					"POST", params);

			Log.d("liveOrderItem /GetPicture/ ItemName sent to php ",
					itemName.toString());
			// check json success tag
			try {
				urlArray = json.getJSONArray(TAG_URLARRAY);
				for (int i = 0; i < urlArray.length(); i++) {
					JSONObject c = urlArray.getJSONObject(i);
					pictureUrl = c.getString(TAG_PICTURE);

					Log.d("LiveOrderItem/GetPicture/doInBg pictureURL",
							pictureUrl.toString());
				}

			} catch (JSONException e) {
				didItWork = false;
				e.printStackTrace();
			}
			return pictureUrl;
		}

		protected void onPostExecute(String pictureUrl) {
			if (pictureUrl.equals("noUrl")) {
				Log.d("LiveOrderItem/GetPicture/onPostEx pictureURL in if",
						pictureUrl.toString());
				Toast error = Toast.makeText(getApplicationContext(),
						"No picture available for this item", Toast.LENGTH_LONG);
				error.show();
			} else if (didItWork == false) {
				Toast error = Toast.makeText(getApplicationContext(),
						"Error connecting to server!", Toast.LENGTH_LONG);
				error.show();
				Log.d("LiveOrderItem/GetPicture/onPostEx pictureURL in elseIf",
						pictureUrl.toString());
			} else {
				Log.d("LiveOrderItem/GetPicture/onPostEx pictureURL in else",
						pictureUrl.toString());
				ivLine.setVisibility(View.VISIBLE);
				ivPicture.setVisibility(View.VISIBLE);
				//get the image saved in the server using the URL gotten above
				new GetImage().execute(pictureUrl);
			}
		}
	}

	class GetImage extends AsyncTask<String, Integer, Bitmap> {
		Boolean didItWork = true;

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			Bitmap bmImg = null;
			URL myImageUrl = null;
			Log.d("LiveOrderItem/GetImage/doInBg pictureURL",
					pictureUrl.toString());
			try {
				myImageUrl = new URL(pictureUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				didItWork = false;
			}
			try {
				HttpURLConnection conn = (HttpURLConnection) myImageUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();

				bmImg = BitmapFactory.decodeStream(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				didItWork = false;
			}
			return bmImg;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (didItWork) {
				//set the immage if no exception is thrown
				ivPicture.setImageBitmap(result);
			} else {
				Toast error = Toast.makeText(getApplicationContext(),
						"Error connecting to server!", Toast.LENGTH_LONG);
				error.show();
			}
		}
	}

	class SaveOrderToDb extends AsyncTask<String, Integer, String> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LiveOrderItem.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			String AdditionalNotes = etAdditionalNotes.getText().toString();
			try {
				int totalAddOnPrice = addOnPrice * quantity;
				PDB.open();
				PDB.orderTableEntry(itemName, restaurantName, quantity, no,
						with, addon, totalAddOnPrice, Date, price, AdditionalNotes);
				PDB.close();
				Log.d("LiveOrderItem/SaveOrderToDb/Finished and Saved",
						itemName + " " + restaurantName + " " + quantity + " "
								+ listStringNo + " " + listStringWith + " "
								+ listStringAddon + " " + totalAddOnPrice + " "
								+ Date + " " + price);
			} catch (Exception e) {
				didItWork = false;
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all items
			pDialog.dismiss();

			if (didItWork) {
				Toast saved = Toast.makeText(getApplicationContext(), "Added "
						+ quantity + " " + itemName + " to your Order",
						Toast.LENGTH_LONG);
				saved.show();
				clear();
			} else {
				Toast notSaved = Toast.makeText(getApplicationContext(),
						"Unable to add " + itemName, Toast.LENGTH_LONG);
				notSaved.show();

			}
		}

	}

	private void GetArrayLists() {
		//get an array for each of the modification types from the phone's database
		PDB = new PhoneDb(LiveOrderItem.this);
		PDB.open();
		arrayWith = PDB.getListForSpinners(restaurantName, "With", 0);
		Log.d("liveOrderItem reached after the WITH array:", "=============");

		arrayNo = PDB.getListForSpinners(restaurantName, "No", 0);
		Log.d("liveOrderItem reached after the NO array:", "=============");

		arrayAddon = PDB.getListForSpinners(restaurantName, "Add on", 1);
		Log.d("liveOrderItem reached after the ADD ON array:", "=============");

		PDB.close();
	}

	private void SetSpinnersOnItemSelectedListener() {
		//set the 3 spinners using the arrays gotten above
		spinnerWith.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				//get the selected value from the spinner
				withSelectedValue = arrayWith[position];

				if (withSelectedValue.equals("With")) {
					//set the value ate the clicked position to empty if the selected value is "with"
					arrayWith[position] = "";

				} else {
					withList.add(withSelectedValue);
				}

				for (String myString : withList) {
					//for each loop that checkes the array for an empty value
					if (myString == "") {
						// do nothing
					} else {
						// concatinate with the previous values stored in the string variabl
						listStringWith = listStringWith + myString + ", ";
						withList.clear();
					}
				}
				try {
					// create a substring of the string variable with all the concatinated values
					//this substring will remove the last comma 
					with = listStringWith.substring(0,listStringWith.length() - 2);
					//a dot will be added at the end
					with = with + ".";
					tvWith.setText(with);
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinnerNo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {

				noSelectedValue = arrayNo[position];
				if (noSelectedValue.equals("No")) {
					arrayNo[position] = "";
				} else {
					noList.add(noSelectedValue);
				}
				for (String myString : noList) {
					if (myString == "") {

					} else {
						listStringNo = listStringNo + myString + ", ";
						noList.clear();
					}
				}
				try {
					no = listStringNo.substring(0, listStringNo.length() - 2);
					no = no + ".";
					tvNo.setText(no);

				} catch (IndexOutOfBoundsException e) {
					// do nothing
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinnerAddon.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {

				addSelectedValue = arrayAddon[position];
				if (addSelectedValue.equals("Add on")) {
					arrayAddon[position] = "";
				} else {
					//using the methode truncateToOriginal we get a mathching value to what is saved in the phone database table
					String OriginalItemName = truncateToOriginal(addSelectedValue);
					Log.d("LiveOrderItem/addon onClickListener/OriginalItemName",
							OriginalItemName);
					//we used the value gotten above to run a query and get the add on price
					PDB.open();
					int tempPrice = PDB.getAddOnPrice(OriginalItemName);
					Log.d(" LiveOrderItem/addon onClickListener addSelectedValue",
							addSelectedValue);
					addOnPrice = addOnPrice + tempPrice;
					PDB.close();
					Log.d(" LiveOrderItem/addon onClickListener addOnPrice",
							addOnPrice + " L.L.");
					addOnList.add(addSelectedValue);
				}

				for (String myString : addOnList) {
					if (myString == "") {

					} else {
						listStringAddon = listStringAddon + myString + ", ";
						addOnList.clear();
					}
				}

				try {
					addon = listStringAddon.substring(0,
							listStringAddon.length() - 2);
					addon = addon + ".";
					tvAddOn.setText(addon);

				} catch (IndexOutOfBoundsException e) {
					// do nothing
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		ArrayAdapter<String> spinerAdapterWith = new ArrayAdapter<String>(
				LiveOrderItem.this, android.R.layout.simple_spinner_item,
				arrayWith);
		spinnerWith.setAdapter(spinerAdapterWith);

		ArrayAdapter<String> spinerAdapterNo = new ArrayAdapter<String>(
				LiveOrderItem.this, android.R.layout.simple_spinner_item,
				arrayNo);
		spinnerNo.setAdapter(spinerAdapterNo);

		ArrayAdapter<String> spinerAdapterAdd = new ArrayAdapter<String>(
				LiveOrderItem.this, android.R.layout.simple_spinner_item,
				arrayAddon);
		spinnerAddon.setAdapter(spinerAdapterAdd);

	}

	public void clear() {

		listStringWith = "";
		tvWith.setText("With");
		listStringNo = "";
		tvNo.setText("No");
		listStringAddon = "";
		tvAddOn.setText("Add on");
		etAdditionalNotes.setText("");

	}

	private String truncateToOriginal(String addSelectedValue) {
		// TODO Auto-generated method stub
		//used to truncate the displayed addOn name to be able to be used for a query
		int indexOfDash = addSelectedValue.indexOf('-');
		String Original = addSelectedValue.substring(0,
				(indexOfDash - 1));
		return Original;
	}

	public void history(View view) {

		Intent i = new Intent(getApplicationContext(), History.class);
		// sending restaurantName and ItemName to next activity
		i.putExtra(TAG_ITEMNAME, itemName);
		i.putExtra(TAG_RESTAURANTNAME, restaurantName);
		// starting new activity
		startActivity(i);
	}

	public void clearWith(View view) {
		//button to clear the with textView
		listStringWith = "";
		tvWith.setText("With");
	}

	public void clearNo(View view) {
		//button to clear the No textView
		listStringNo = "";
		tvNo.setText("No");
	}

	public void clearAddOn(View view) {
		//button to clear the AddOn textView
		listStringAddon = "";
		tvAddOn.setText("Add on");
	}

	public void clear(View view) {

		clear();
	}

	public void addItem(View view) {
		// gets a detailed current date
		Calendar c = Calendar.getInstance();
		int seconds = c.get(Calendar.SECOND);
		int minutes = c.get(Calendar.MINUTE);
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int year = c.get(Calendar.YEAR);

		if ((minutes < 10) && (seconds < 10)) {
			Date = (day + "/" + (month + 1) + "/" + year + " at " + hours
					+ ":0" + minutes + ":0" + seconds);
		} else if (minutes < 10) {
			Date = (day + "/" + (month + 1) + "/" + year + " at " + hours
					+ ":0" + minutes + ":" + seconds);
		} else if (seconds < 10) {
			Date = (day + "/" + (month + 1) + "/" + year + " at " + hours + ":"
					+ minutes + ":0" + seconds);
		} else {
			Date = (day + "/" + (month + 1) + "/" + year + " at " + hours + ":"
					+ minutes + ":" + seconds);
		}
		//save the data to the order table
		new SaveOrderToDb().execute();
		finish();
	}

	public void addQuantity(View view) {
		//when the button '+' is clicked
		if (quantity == 14) {
			warning = Toast.makeText(getApplicationContext(), "Warning: "
					+ (quantity + 1)
					+ " is a large ammount, please be carefull",
					Toast.LENGTH_LONG);

			warning.show();

			quantity = quantity + 1;
			bQuantity.setText("Quantity : " + quantity);

		} else {
			quantity = quantity + 1;
			bQuantity.setText("Quantity : " + quantity);
			bQuantityMinus.setClickable(true);
		}

	}

	public void deductQuantity(View view) {
		//if the button '-' is clicked
		if (quantity == 1) {
			bQuantityMinus.setClickable(false);
		} else {
			quantity = quantity - 1;
			bQuantity.setText("Quantity : " + quantity);

		}
	}
}
