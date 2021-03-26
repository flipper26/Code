package com.application.orderAp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpenseCalculating extends Activity {

	Spinner spinnerRestaurants;
	TextView tv_expenceResult, tvChooseRest, tvExpenseChangingText, tv_youSpent;
	CheckBox checkBoxManualItems;
	RadioButton rAllRest, rOneRest;
	Button bExpenseCalculate, bClearExpenseLayout;

	String[] restaurantsArray;
	String selectedRestaurant;
	String expenseTotal;

	OnClickListener checkBoxListener;
	PhoneDb db = new PhoneDb(this);

	Toast warning;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_expense);
		initialize();
		getRestaurantsArray();
		setSpinner();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		
		spinnerRestaurants = (Spinner) findViewById(R.id.spinnerRestaurants);
		spinnerRestaurants.setEnabled(false);

		tv_expenceResult = (TextView) findViewById(R.id.tv_expenseResult);
		tvChooseRest = (TextView) findViewById(R.id.tvChooseRest);
		tvExpenseChangingText = (TextView) findViewById(R.id.tvExpenseChangingText);
		tv_youSpent = (TextView) findViewById(R.id.tv_youSpent);
		
		rOneRest = (RadioButton) findViewById(R.id.rOneRest);
		rOneRest.setChecked(false);
		
		rAllRest = (RadioButton) findViewById(R.id.rAllRest);
		rAllRest.setChecked(true);
		
		checkBoxManualItems = (CheckBox) findViewById(R.id.checkBoxManualItems);
		checkBoxManualItems.setChecked(false);
		
		bExpenseCalculate = (Button) findViewById(R.id.bExpenseCalculate);
		bClearExpenseLayout = (Button) findViewById(R.id.bClearExpenseLayout);
		
		//Setting fonts
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),
				"fifawelcome.ttf");
		Typeface fontsuperhero = Typeface.createFromAsset(getAssets(),
				"superhero.ttf");
		
		tv_expenceResult.setTypeface(fontsuperhero);
		tvChooseRest.setTypeface(fontfifa);
		tvChooseRest.setTypeface(fontfifa);
		tvExpenseChangingText.setTypeface(fontfifa);
		tv_youSpent.setTypeface(fontfifa);
		rOneRest.setTypeface(fontfifa);
		rAllRest.setTypeface(fontfifa);
		checkBoxManualItems.setTypeface(fontfifa);
		bExpenseCalculate.setTypeface(fontfifa);
		bClearExpenseLayout.setTypeface(fontfifa);
		
		
		// creating an on checkedChangeListener for the radioGroup
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rAllRest: {
					spinnerRestaurants.setEnabled(false);
					checkBoxManualItems.setEnabled(true);
				}
				break;
				case R.id.rOneRest: {
					spinnerRestaurants.setEnabled(true);
					checkBoxManualItems.setChecked(false);
					checkBoxManualItems.setEnabled(false);
				}
				}
			}
		});
	}

	private void getRestaurantsArray() {
		// TODO Auto-generated method stub
		//getting the list of saved restaurants from the history table
		db.open();
		restaurantsArray = db.getRestaurantsfromHistory();
		db.close();
		Log.d("EXPENSE CALCULATING --- restaurants array :",
				restaurantsArray.toString());

	}
	//Setting the spinner using the array gotten above
	public void setSpinner() {
		spinnerRestaurants
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long id) {

						selectedRestaurant = restaurantsArray[position];

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		ArrayAdapter<String> spinerAdapter = new ArrayAdapter<String>(
				ExpenseCalculating.this, android.R.layout.simple_spinner_item,
				restaurantsArray);
		spinnerRestaurants.setAdapter(spinerAdapter);
	}

	public void calculate(View view) {
		int Total = 0;
		boolean check;
		// checkes if the table history is empty
		db.open();
		check = db.isThisTableEmpty("History");
		db.close();
		
		if(!check){
			//if not empty
			if (rOneRest.isChecked()) {
				//check if one restaurant is selected and display
				Total = calculateOneRestaurant(selectedRestaurant);
				tv_youSpent.setVisibility(View.VISIBLE);
				tv_expenceResult.setText(Total + " L.L.");
			} else if (rAllRest.isChecked()) {
				//check if all restaurants is selected
				Total = calculateAllRestaurants();
				if (checkBoxManualItems.isChecked()) {
					//check if the manual items is selected or not and display
					int ManualTotal = calculateManuallyAdded();
					int TotalToDisplay = Total + ManualTotal;
					tv_youSpent.setVisibility(View.VISIBLE);
					tv_expenceResult.setText(Total +" + "+ ManualTotal + " = " + TotalToDisplay + " L.L.");
				} else {
					tv_youSpent.setVisibility(View.VISIBLE);
					tv_expenceResult.setText(Total + " L.L.");
				}
			}
		}
		else{
			//if the table is empty display a message
			warning = Toast.makeText(getApplicationContext(),
					"There is no items ordered",
					Toast.LENGTH_LONG);
			warning.show();
		}
	}

	public void ClearExpense(View view) {
		rOneRest.setChecked(false);
		rAllRest.setChecked(true);
		checkBoxManualItems.setChecked(false);
		tv_expenceResult.setText("");
		tv_youSpent.setVisibility(View.GONE);
	}

	private int calculateOneRestaurant(String restaurantName) {
		//get the sum of the expense of one selected restaurant givien as an argument
		db.open();
		int totalExpense = db.calculateOneRestaurantExpense(selectedRestaurant);
		db.close();
		return totalExpense;

	}

	private int calculateAllRestaurants() {
		//get the sum of the expense of all restaurants
		db.open();
		int totalExpense = db.calculateAllRestaurantsExpense();
		db.close();
		return totalExpense;

	}

	private int calculateManuallyAdded() {
		//get the sum of the expense of all manuallyAddedItems
		db.open();
		int totalExpense = db.calculateManuallyAddedItemsExpense();
		return totalExpense;

	}

}
