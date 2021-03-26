package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class History extends Activity {

	public static final String KEY_INGREDIENTS = "Ingredients";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_CATEGORY = "Category";
	public static final String KEY_RESTAURANT = "RestaurantName";
	public static final String KEY_PERSONALRATING = "PersonalRating";
	public static final String KEY_TIMESORDERED = "TimesOrdered";
	public static final String KEY_DATE = "Date";
	public static final String KEY_ITEMNAME = "ItemName";
	
	
	private static final String TAG_SUCCESS = "success";
	private static final String url_post_rating_items = "http://192.168.1.2/OrderAp_php/rate_item.php?ItemName=itemName&PersonalRating=userRating";
	
	int userRating = 0;
	
	
	ProgressDialog pDialog;
	
	String itemName, restaurantName;
	String historyItemName, historyTimesOrdered, historyPersonalRating, historyDate;
	
	TextView tv_historyItemName;
	TextView tv_historyOrdered, tv_historyDate;
	TextView tvComparingChangingText;
	Button bRateIt;
	RatingBar bRate;
	PhoneDb db = new PhoneDb(this);
	
	boolean checkRating, didItWork = true;
	
	JSONParser jsonParser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_history);
		getExtras();
		initialize();
		getInfo();
		displayInfo();
	}
	public void getExtras(){
		//obtain data from the previous activity "Comparing" to use in a query
		Intent in = this.getIntent();
		restaurantName = in.getStringExtra(KEY_RESTAURANT);
		itemName = in.getStringExtra(KEY_ITEMNAME);
	}
	
	public void initialize(){
		// checking to see if this item was rated before
		db.open();
		int savedRating = db.getPersonalRating(itemName);
		db.close();
		if(savedRating == 0){
			checkRating = false;
		}else{
			checkRating = true;
			
		}
		
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),"fifawelcome.ttf");
		
		//initializing button
		bRateIt = (Button)findViewById(R.id.bRateIt);
		bRate = (RatingBar)findViewById(R.id.bRate);
		
		//initializing, adding font to the changing text at the top of the activity's page.
		tvComparingChangingText = (TextView)findViewById(R.id.tvComparingChangingText);
		tvComparingChangingText.setTypeface(fontfifa);
		
		//initializing, adding font and giving value to non-modifiable TextViews.
		tv_historyItemName = (TextView)findViewById(R.id.tv_historyItemName);
		tv_historyItemName.setText(itemName);
		tv_historyItemName.setTypeface(fontfifa);
		
		//initializing first item's info textViews to be given values on runtime and adding font.
	
		tv_historyOrdered = (TextView)findViewById(R.id.tv_historyTimesOrdered);
		tv_historyDate = (TextView)findViewById(R.id.tv_historyDate);
		
		tv_historyOrdered.setTypeface(fontfifa);
		tv_historyDate.setTypeface(fontfifa);

	}
	
	public void getInfo(){
	//getting info from the database using 2 variables obtained from the previous activity.
	HashMap<String, String> ItemMap = new HashMap<String, String>();
	db.open();
	ItemMap = db.getItemData(itemName, restaurantName);
	db.close();
	historyTimesOrdered = ItemMap.get(KEY_TIMESORDERED);
	historyPersonalRating = ItemMap.get(KEY_PERSONALRATING);
	historyDate = ItemMap.get(KEY_DATE);
	Log.d("History/getInfo , the rating saved in the phonedb is :", historyPersonalRating.toString());
	}
	
	public void displayInfo(){
		
		//displaying item info in textviews.
		tv_historyOrdered.setText(historyTimesOrdered);
		tv_historyDate.setText(historyDate);
		
		if(checkRating){
			float floathistoryPersonalRating = Float.parseFloat(historyPersonalRating);
			bRate.setRating(floathistoryPersonalRating);
			Log.d("History/displayInfo/historyTimesOrdered",String.valueOf(historyPersonalRating));
			Log.d("History/displayInfo/floatHistoryTimesOrdered",String.valueOf(floathistoryPersonalRating));
			bRateIt.setVisibility(View.GONE);
			bRate.setIsIndicator(true);

			
		}else if(checkRating == false){
				//do nothing
		
		}
			
	}
	
	public void openRatingDialog (View view){
		
		//button used to rate
		
		userRating = bRate.getProgress();
		Log.d("History/ShowDialog for rating : the rating clicked is:",String.valueOf(userRating));
		if(userRating == 0){
			Toast error = Toast.makeText(getApplicationContext(),
					"0 stars ? Slide your finger across the stars to rate",
					Toast.LENGTH_LONG);
			error.show();
		}else{
			ShowDialog();	
		}
		
	}
	public void ShowDialog(){
		//confirmation of rating plus tip
		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
		switch (userRating){
		case 1:
			popDialog.setTitle(userRating + " stars, are you sure ?");
			popDialog.setMessage("Tip: Might want to tell your waiter what was wrong with your "+ itemName +".");
			break;
		case 2:
			popDialog.setTitle(userRating +" stars, are you sure ?");
			popDialog.setMessage("Tip: Try the "+ itemName +" some other time, might change your mind.");
			break;
		case 3:
			popDialog.setTitle(userRating +" stars, are you sure ?");
			popDialog.setMessage("Tip: You like it, tell your waiter what could make the "+ itemName + " even better.");
			break;
		case 4:
			popDialog.setTitle(userRating +" stars, are you sure ?");
			popDialog.setMessage("Tip: If you think it deserves 4 stars, others might too, spread the word !");
			break;
		case 5:
			popDialog.setTitle(userRating +" stars, are you sure ?");
			popDialog.setMessage("Tip: Tell everybody about it , you wouldn't want them to miss such an amazing "+ itemName +".");
			break;
		}

		// Button OK
		popDialog.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						
						dialog.dismiss();
						bRate.setRating(userRating);
						bRate.setIsIndicator(true);
						bRateIt.setVisibility(View.GONE);
						//saving rating to phone database
						db.open();
						db.setPersonalRating(itemName, userRating);
						db.close();
						
						Toast error = Toast.makeText(getApplicationContext(),
								"Thank you for rating.",
								Toast.LENGTH_LONG);
						error.show();
						// saving rating to server database
						new SaveRatingToServerDb().execute();
					}

				})

		// Button Cancel
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		popDialog.create();
		popDialog.show();
        
	}

class SaveRatingToServerDb extends AsyncTask<String, String, String> {
	
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(History.this);
			pDialog.setIndeterminate(false);
			pDialog.setMessage("Saving your rating ...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(KEY_ITEMNAME, itemName));			
			params.add(new BasicNameValuePair(KEY_PERSONALRATING, userRating+""));
			
			Log.d("History/SaveRatingToServerDb/doInBackground/params", params.toString());

			// sending modified data through http request

			JSONObject json = jsonParser.makeHttpRequest(
					url_post_rating_items, "POST", params);

			Log.d("History/SaveRatingToServerDb/doInBackground/json", json.toString());

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {

					Log.d("History/ do in background ", "entered success=  1");
					
				} else {

					Log.d("History/ do in background ", "entered success=  0");
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
			// dismiss the dialog once product updated
			pDialog.dismiss();
			if (didItWork == false) {
				Toast error = Toast.makeText(getApplicationContext(),
						"Error: failed to connect, please try again :",
						Toast.LENGTH_LONG);
				error.show();

			} 
		}
	}
	
}
