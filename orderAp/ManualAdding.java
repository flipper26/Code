package com.application.orderAp;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ManualAdding extends Activity {
	
	// declaring variables *******************************************************************
	EditText etRestaurantName, etPrice, etItemName;
	TextView tvMessageAdd, backgroundText, tvManulAdding;
	Button bshow, bAdd, bClearDb;
	
	String restName, itemName, price;
	
	boolean didItWork = true;
	boolean addItemToDbFinished;
	
	int roundedPrice;
	//****************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_manual_adding);
		initialize();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		// finiding Views and linking them to Java objects
		
		etRestaurantName = (EditText) findViewById(R.id.etRestaurantName);
		etPrice = (EditText) findViewById(R.id.etPrice);
		etItemName = (EditText) findViewById(R.id.etItemName);
		tvMessageAdd = (TextView) findViewById(R.id.tvMessageAdd);
		tvManulAdding = (TextView) findViewById(R.id.tvManulAdding);
		bshow = (Button) findViewById(R.id.bshow);
		bAdd = (Button) findViewById(R.id.bManualAdd);
		bClearDb = (Button) findViewById(R.id.bManualClearDb);
		
		// Setting the font
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),"fifawelcome.ttf");
		tvMessageAdd.setTypeface(fontfifa);
		tvManulAdding.setTypeface(fontfifa);
		bshow.setTypeface(fontfifa);
		bAdd.setTypeface(fontfifa);
		bClearDb.setTypeface(fontfifa);

	}

	private void getStrings() {
		// gets the string values from the edit text boxes
		
		restName = etRestaurantName.getText().toString();
		price = etPrice.getText().toString();
		int	priceNoLeftZeros = Integer.parseInt(price);
		String priceToSwitch = priceNoLeftZeros+"";
		switch (priceToSwitch.length()){
			case 3 :{
			int	intPrice = Integer.parseInt(price);
			roundedPrice = round3(intPrice);
			}
			break;
			case 4 :{
			int	intPrice = Integer.parseInt(price);
			roundedPrice = round4(intPrice);
			}
			break;
			case 5 :{
			int	intPrice = Integer.parseInt(price);
			roundedPrice = round5(intPrice);
			}
		}
		
		itemName = etItemName.getText().toString();
	}

	private int round3(int number) {
		// TODO Auto-generated method stub
		
		// rounding method for values that has 3 numbers
		int numToReturn = 0;
		
			  if(number == 0){
			numToReturn = 0;	
			
		}else if (number > 0 && number < 125){
			numToReturn = 0;
			
		}else if (number >= 125 && number < 250){
			numToReturn = 250;
			
		}else if(number >= 250 && number < 375){
			numToReturn = 250;
		
		}else if(number >= 375 && number < 500){
			numToReturn = 500;
		
		}else if(number >= 500 && number < 675){
			numToReturn = 500;
		
		}else if(number >= 675 && number < 750){
			numToReturn = 750;
		
		}else if(number >= 750 && number < 875){
			numToReturn = 750;
		
		}else if(number >= 875 && number < 1000){
			numToReturn = 1000;
		
		}
		return numToReturn;
	}

	private int round4(int number) {
		//TODO  rounding method for values that has 4 numbers
		
		if(number == 0){
			return 0;
		}else{
		String StringRoundResult = null;
		String numString = number + "";
		String firstNumber = numString.substring(0, 1);
		int firstNum = Integer.parseInt(firstNumber);
		String lastThreeNumbers = numString.substring(1, 4);
		Log.d("round4/firstNumber and lastThreeNumbers",firstNumber + " " + lastThreeNumbers );
		int lastNumbers = Integer.parseInt(lastThreeNumbers);
		int rounded3 = round3(lastNumbers);
		 if(rounded3 == 1000){
			 firstNum ++;
			 StringRoundResult = firstNum + "000";
		 }else if (rounded3 == 0){
			 StringRoundResult = firstNum + "000";
		 }else{
			 StringRoundResult = "" + firstNum + rounded3;
		 }
		 int roundResult = Integer.parseInt(StringRoundResult);
		return roundResult;
		}
	}
	
	
private int round5(int number) {
	//TODO  rounding method for values that has 5 numbers	
	
		String StringRoundResult = null;
		int roundResult = 0;
		String result = null;
		String stringRounded4;
		String numString = number + "";
		int rounded4 = 0;
		String firstNumber = numString.substring(0, 1);
		int firstNum = Integer.parseInt(firstNumber);
		String lastFourNumbers = numString.substring(1, 5);
		int lastNumbers = Integer.parseInt(lastFourNumbers);
		Log.d("round5/firstNumber and lastFourNumbers",firstNumber + " " + lastFourNumbers );
		if(lastNumbers <= 999 && lastNumbers >= 250){
			stringRounded4 = roundWithLeftZero("0"+lastNumbers);
			result = firstNumber + stringRounded4 + "";
			roundResult = Integer.parseInt(result);
		}else if (lastNumbers < 250 && lastNumbers >0){
			result = firstNumber + "0000";
			roundResult = Integer.parseInt(result);
		}else{
			rounded4 = round4(lastNumbers);
		
		 if(rounded4 == 10000){
			 firstNum ++;
			 StringRoundResult = firstNum + "0000";
		 }else if (rounded4 == 0){
			 StringRoundResult = firstNum + "0000";
		 }else{
			 StringRoundResult = "" + firstNum + rounded4;
		 }
		
		 roundResult = Integer.parseInt(StringRoundResult);
		}
		return roundResult;
		}

private String roundWithLeftZero(String number) {
	//TODO  rounding method for values that has 4 numbers
	String StringRoundResult = null;
	String firstNumber = number.substring(0, 1);
	int firstNum = Integer.parseInt(firstNumber);
	String lastThreeNumbers = number.substring(1, 4);
	Log.d("roundWithLeftZero/firstNumber and lastThreeNumbers",firstNumber + " " + lastThreeNumbers );
	int lastNumbers = Integer.parseInt(lastThreeNumbers);
	int rounded3 = round3(lastNumbers);
	 if(rounded3 == 1000){
		 firstNum ++;
		 StringRoundResult = firstNum + "000";
	 }else if (rounded3 == 0){
		 StringRoundResult = firstNum + "000";
	 }else{
		 StringRoundResult = "" + firstNum + rounded3;
	 }
	 
	return StringRoundResult;

}
	
	public void bClear(View view) {
		// clear button listener
		clear();
		
	}

	private void clear() {
		// TODO Clears all the edit texts
		

		etRestaurantName.setText(null);
		etPrice.setText(null);
		etItemName.setText(null);
		etRestaurantName.requestFocus();
	}

	public void addItem(View view) {
		// TODO buttin addItem listener that checks that all conditions are met and than execute the get srtings and the show dialog
if((etItemName.getText().toString().equals("")) && ((etRestaurantName.getText().toString().equals(""))) && ((etPrice.getText().toString().equals("")))){
	Toast error = Toast.makeText(getApplicationContext(),
			" All the fields are required", Toast.LENGTH_LONG);
	error.show();
}else if((etItemName.getText().toString().equals("")) && ((etRestaurantName.getText().toString().equals("")))){
	Toast error = Toast.makeText(getApplicationContext(),
			"Restaurant name and Item's name are required", Toast.LENGTH_LONG);
	error.show();
}else if((etItemName.getText().toString().equals("")) && ((etPrice.getText().toString().equals("")))){
	Toast error = Toast.makeText(getApplicationContext(),
			"Item's name and Price is required", Toast.LENGTH_LONG);
	error.show();
}else if((etRestaurantName.getText().toString().equals("")) && ((etPrice.getText().toString().equals("")))){
	Toast error = Toast.makeText(getApplicationContext(),
			"Restaurant name and Price are required", Toast.LENGTH_LONG);
	error.show();
			
}else if(etRestaurantName.getText().toString().equals("")){
	Toast error = Toast.makeText(getApplicationContext(),
			"Restaurant name is required", Toast.LENGTH_LONG);
	error.show();
}else if(etItemName.getText().toString().equals("")){
	Toast error = Toast.makeText(getApplicationContext(),
			"Item's name is required", Toast.LENGTH_LONG);
	error.show();
	
}else if(etPrice.getText().toString().equals("")){
	Toast error = Toast.makeText(getApplicationContext(),
			"Price is required", Toast.LENGTH_LONG);
	error.show();
	
}else if((Integer.parseInt(etPrice.getText().toString())>50000)||(Integer.parseInt(etPrice.getText().toString())<250) ){
	Toast error = Toast.makeText(getApplicationContext(),
			"A price must be between 250 and 50 000", Toast.LENGTH_LONG);
	error.show();
	
}else{
	getStrings();
	ShowDialog();
}


	}
	
	public void ShowDialog(){
		//TODO shows the confirmation dialog along with the rounded number for confirmation
		
		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
		
			popDialog.setTitle("Are you sure ?");
			popDialog.setMessage("You are adding " + restName + "'s item: " + itemName + ", with a price of: " + roundedPrice);
		

		// Button OK
		popDialog.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						
						
						dialog.dismiss();
						// execute the addItemToDbFinished which adds the item's info to the manuallyAddedItems table
						new addItemToDbFinished().execute();
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
	public void openManualAddedItemsList(View view){
		// 'show' button listener, starts the new activity
		Intent in = new Intent(getApplicationContext(), ManuallyAddedItemsList.class);
        startActivity(in);
	}

	class addItemToDbFinished extends AsyncTask<String, Integer, String> {
		
		// adds the items info to the table ManuallyAddedItems
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ManualAdding.this);
			pDialog.setMessage("Loading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				PhoneDb db = new PhoneDb(ManualAdding.this);
				db.open();
				// putting the info in the table
				db.manuallyAddedItemsTableEntryFromManualAdding(itemName, restName, roundedPrice+"");
				db.close();
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
				Toast saved = Toast.makeText(getApplicationContext(),
						"Item added", Toast.LENGTH_LONG);
				saved.show();
				clear();
			} else {
				Toast notSaved = Toast.makeText(getApplicationContext(),
						"Unable to add " + itemName, Toast.LENGTH_LONG);
				notSaved.show();
			}
			addItemToDbFinished = true;
		}

	}

}
