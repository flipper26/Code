package com.application.orderAp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OrderApMenu extends Activity implements View.OnClickListener{
	// Declaring variables
	
	Button liveOrder, comparing, expenseCalculating, restaurant, manualAdding,stopWatch, order;
	TextView backgroundText;
	
	boolean isEmpty;
	
// initializing jsonParser and PhoneDb objects
	JSONParser jsonParser = new JSONParser();
	PhoneDb db = new PhoneDb(this);
	
	//Declaring static variables
	private static final String TONEXTACTIVITY= "toNextActivity";
	private static final String TABLE_ITEMS = "items";
	public static final String KEY_FROMLIVEORDER = "fromLiveOrder";
	public static final String KEY_CONNECTIONSTATUS = "connectionStatus";
	public static final String URL_CHECKCONNECTION = "http://192.168.1.2/OrderAp_php/check_connection.php";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_orderap_menu);
		initialize();
		// Setting onClickListener for the buttons
		liveOrder.setOnClickListener(this);
		comparing.setOnClickListener(this);
		expenseCalculating.setOnClickListener(this);
		restaurant.setOnClickListener(this);
		manualAdding.setOnClickListener(this);
		stopWatch.setOnClickListener(this);
		order.setOnClickListener(this);
	}
	

	private void initialize() {
		// TODO Initializing the buttons and setting the font
		liveOrder = (Button) findViewById (R.id.liveOrder);
		order = (Button) findViewById (R.id.order);
		expenseCalculating = (Button) findViewById (R.id.expenseCalculating);
		comparing = (Button) findViewById (R.id.comparing);
		stopWatch = (Button) findViewById (R.id.stopWatch);
		restaurant = (Button) findViewById (R.id.restaurants);
		manualAdding = (Button) findViewById (R.id.manualAdding);
		
		backgroundText = (TextView) findViewById (R.id.backgroundTextMenu);
		
		
		Typeface fontfifa = Typeface.createFromAsset(getAssets(), "fifawelcome.ttf");  
		backgroundText.setTypeface(fontfifa);
		liveOrder.setTypeface(fontfifa);
		comparing.setTypeface(fontfifa); 
		expenseCalculating.setTypeface(fontfifa); 
		restaurant.setTypeface(fontfifa); 
		manualAdding.setTypeface(fontfifa); 
		stopWatch.setTypeface(fontfifa); 
		order.setTypeface(fontfifa);
		
	}

	@Override
	public void onClick(View v) {
		// TODO implementing the onClick method for the buttons.
		switch (v.getId()){
		case R.id.liveOrder:
			WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			if(wifi.isWifiEnabled()){
				// checking for the connection
				//new checkConnection().execute();
				Intent inlive = new Intent(getApplicationContext(), LiveOrderCategoryMenu.class);
	            startActivity(inlive);
	            
			}else if(!wifi.isWifiEnabled()){
				Intent inWifi = new Intent(getApplicationContext(), WifiConnection.class);
	            startActivity(inWifi);
			}break;
			
		case R.id.expenseCalculating:
			Intent inExpenseCalculating = new Intent(getApplicationContext(), ExpenseCalculating.class);
            startActivity(inExpenseCalculating);
			break;
			
		case R.id.order:
			Intent inOrder = new Intent(getApplicationContext(), OrderActivity.class);
            startActivity(inOrder);
			break;			
			
		case R.id.comparing:
			// checking if the table Items is empty before opening the comparing activity
			db.open();
			isEmpty = db.isThisTableEmpty(TABLE_ITEMS);
			db.close();
			if(isEmpty){
				Toast NoDataSavedYet = Toast.makeText(getApplicationContext(),
						"Nothing to compare yet", Toast.LENGTH_LONG);
				NoDataSavedYet.show();
			}else{
			Intent in = new Intent(getApplicationContext(),Comparing.class);
            startActivity(in);
			}break;
			
		case R.id.stopWatch:
			Intent inStopWatch = new Intent(getApplicationContext(), StopWatch.class);
			inStopWatch.putExtra(KEY_FROMLIVEORDER, 0);
            startActivity(inStopWatch);
			break;
			
		
			
		case R.id.manualAdding:
			Intent inAdditems = new Intent(getApplicationContext(), ManualAdding.class);
            startActivity(inAdditems);
			break;
			
		
			
		case R.id.restaurants:
			Intent inRest = new Intent(getApplicationContext(), SavedRestaurantNames.class);
			String a = restaurant.getText().toString();
			
			Log.d("string put in intent", restaurant.getText().toString());
			inRest.putExtra(TONEXTACTIVITY, a);
            startActivity(inRest);
            
			break;
		}

		}
	
	class checkConnection extends AsyncTask<String, Integer, String> {
// TODO Checks the connection by connecting to a php  file before opening the Category menu
		boolean didItWork = true;
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			pDialog = new ProgressDialog(OrderApMenu.this);
			pDialog.setMessage("Connecting to Restaurant ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Log.d("reached here while connecting","/-/-/-/-/-/-/");
			// making http request to catch any thrown exception
			JSONObject json = jsonParser.makeHttpRequest(
					URL_CHECKCONNECTION, "GET", params);
			Log.d("Reached after the httpRequest:","/*-/*-/*-/*-/*--*/-*/-*/-*/");
			if(json == null){
				Log.d("Error Connecting", "*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
				Toast error = Toast.makeText(getApplicationContext(),
						"Error: No connection available!",
						Toast.LENGTH_LONG);
				error.show();
				didItWork = false;
			}else{
				Log.d("it worked","/*/*/*/*/*/");
				didItWork = true;
			// check json success tag
			/*try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully got information
					status = json.getString(KEY_CONNECTIONSTATUS);
					Log.d("OrderApMenu/ checkConnection/ string from php is: ",status);
				} else {
					Log.d(" OrderApMenu / could not connect","ERROR Connecting !!!!!!!!");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d(" OrderApMenu / could not connect","ERROR Connecting !!!!!!!!");
			}*/
			}

			return null;
		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all Items
			pDialog.dismiss();
			if (didItWork == true) {
				// starting the next activity
				Intent inlive = new Intent(getApplicationContext(), LiveOrderCategoryMenu.class);
	            startActivity(inlive);
			}

			else {
				Toast error = Toast.makeText(getApplicationContext(),
						"Error: Failed to connect!",
						Toast.LENGTH_LONG);
				error.show();
			}
		}
	}
}

