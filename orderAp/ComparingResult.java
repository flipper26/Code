package com.application.orderAp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ComparingResult extends Activity {

	private final static String TAG_FIRST_RESTAURANT = "firstRestaurantName";
	private final static String TAG_SECOND_RESTAURANT = "secondRestaurantName";
	private final static String TAG_FIRST_ITEMNAME = "firstItemName";
	private final static String TAG_SECOND_ITEMNAME = "secondItemName";
	public static final String KEY_INGREDIENTS = "Ingredients";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_CATEGORY = "Category";
	public static final String KEY_RESTAURANT = "RestaurantName";
	public static final String KEY_PERSONALRATING = "PersonalRating";
	public static final String KEY_TIMESORDERED = "TimesOrdered";
	public static final String KEY_DATE = "Date";
	public static final String KEY_ITEMNAME = "ItemName";
	
	String firstItemName, firstRestaurantName, secondRestaurantName, secondItemName;
	
	String firstItemItemName, firstItemRestaurant, firstItemIngredients, firstItemPrice, firstItemCategory, firstItemTimesOrdered, firstItemPersonalRating, firstItemDate;
	String secondItemItemName, secondItemRestaurant, secondItemIngredients, secondItemPrice, secondItemCategory, secondItemTimesOrdered, secondItemPersonalRating, secondItemDate;
	
	TextView tv_compResultFirstItemName, tv_compResultSecondItemName;
	
	TextView tv_compResultFirstItemItemName,tv_compResultFirstItemRestaurant, tv_compResultFirstItemIngredients, tv_compResultFirstItemPrice, tv_compResultFirstItemCategory, tv_compResultFirstItemTimesOrdered, tv_compResultFirstItemPersonalRating, tv_compResultFirstItemDate;
	TextView tv_compResultSecondItemItemName,tv_compResultSecondItemRestaurant, tv_compResultSecondItemIngredients, tv_compResultSecondItemPrice, tv_compResultSecondItemCategory, tv_compResultSecondItemTimesOrdered, tv_compResultSecondItemPersonalRating, tv_compResultSecondItemDate;
	
	TextView tvComparingChangingText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_comparing_result);
		getExtras();
		initialize();
		getInfo();
		displayInfo();
	}
	public void getExtras(){
		//obtain data from the previous activity "Comparing" to use in a query
		Intent in = this.getIntent();
		firstRestaurantName = in.getStringExtra(TAG_FIRST_RESTAURANT);
		firstItemName = in.getStringExtra(TAG_FIRST_ITEMNAME);
		secondRestaurantName = in.getStringExtra(TAG_SECOND_RESTAURANT);
		secondItemName = in.getStringExtra(TAG_SECOND_ITEMNAME);		
	}
	
	public void initialize(){
		
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),"fifawelcome.ttf");
		
		//initializing, adding font to the changing text at the top of the activity's page.
		tvComparingChangingText = (TextView)findViewById(R.id.tvComparingChangingText);
		tvComparingChangingText.setTypeface(fontfifa);
		
		//initializing, adding font and giving value to non-modifiable TextViews.
		tv_compResultFirstItemName = (TextView)findViewById(R.id.tv_compResultFirstItemName);
		tv_compResultSecondItemName= (TextView)findViewById(R.id.tv_compResultSecondItemName);
		tv_compResultFirstItemName.setText(firstItemName);
		tv_compResultSecondItemName.setText(secondItemName);
		tv_compResultFirstItemName.setTypeface(fontfifa);
		tv_compResultSecondItemName.setTypeface(fontfifa);
		
		//initializing first item's info textViews to be given values on runtime and adding font.
		tv_compResultFirstItemRestaurant = (TextView)findViewById(R.id.tv_compResultFirstItemRestaurant);
		tv_compResultFirstItemIngredients = (TextView)findViewById(R.id.tv_compResultFirstItemIngredients);
		tv_compResultFirstItemPrice = (TextView)findViewById(R.id.tv_compResultFirstItemPrice);
		tv_compResultFirstItemCategory = (TextView)findViewById(R.id.tv_compResultFirstItemCategory);
		tv_compResultFirstItemTimesOrdered = (TextView)findViewById(R.id.tv_compResultFirstItemTimesOrdered);
		tv_compResultFirstItemPersonalRating = (TextView)findViewById(R.id.tv_compResultFirstItemPersonalRating);
		tv_compResultFirstItemDate = (TextView)findViewById(R.id.tv_compResultFirstItemDate);
		
		tv_compResultFirstItemRestaurant.setTypeface(fontfifa);
		tv_compResultFirstItemIngredients.setTypeface(fontfifa);
		tv_compResultFirstItemPrice.setTypeface(fontfifa);
		tv_compResultFirstItemCategory.setTypeface(fontfifa);
		tv_compResultFirstItemTimesOrdered.setTypeface(fontfifa);
		tv_compResultFirstItemPersonalRating.setTypeface(fontfifa);
		tv_compResultFirstItemDate.setTypeface(fontfifa);
		
		//initializing Second item's info textViews to be given values on runtime and adding font.
		tv_compResultSecondItemRestaurant = (TextView)findViewById(R.id.tv_compResultSecondItemRestaurant);
		tv_compResultSecondItemIngredients = (TextView)findViewById(R.id.tv_compResultSecondItemIngredients);
		tv_compResultSecondItemPrice = (TextView)findViewById(R.id.tv_compResultSecondItemPrice);
		tv_compResultSecondItemCategory = (TextView)findViewById(R.id.tv_compResultSecondItemCategory);
		tv_compResultSecondItemTimesOrdered = (TextView)findViewById(R.id.tv_compResultSecondItemTimesOrdered);
		tv_compResultSecondItemPersonalRating = (TextView)findViewById(R.id.tv_compResultSecondItemPersonalRating);
		tv_compResultSecondItemDate = (TextView)findViewById(R.id.tv_compResultSecondItemDate);
		
		tv_compResultSecondItemRestaurant.setTypeface(fontfifa);
		tv_compResultSecondItemIngredients.setTypeface(fontfifa);
		tv_compResultSecondItemPrice.setTypeface(fontfifa);
		tv_compResultSecondItemCategory.setTypeface(fontfifa);
		tv_compResultSecondItemTimesOrdered.setTypeface(fontfifa);
		tv_compResultSecondItemPersonalRating.setTypeface(fontfifa);
		tv_compResultSecondItemDate.setTypeface(fontfifa);
	}
	
	public void getInfo(){
	//getting info from the database using 2 variables obtained from the previous activity.
	PhoneDb db = new PhoneDb(this);	
	
	//first item info
	HashMap<String, String> firstItemMap = new HashMap<String, String>();
	db.open();
	firstItemMap = db.getItemData(firstItemName, firstRestaurantName);
	db.close();
	firstItemRestaurant = firstItemMap.get(KEY_RESTAURANT);
	firstItemIngredients = firstItemMap.get(KEY_INGREDIENTS);
	firstItemPrice = firstItemMap.get(KEY_PRICE);
	firstItemCategory = firstItemMap.get(KEY_CATEGORY);
	firstItemTimesOrdered = firstItemMap.get(KEY_TIMESORDERED);
	firstItemPersonalRating = firstItemMap.get(KEY_PERSONALRATING);
	Log.d("ComparingResult/ the personal rating from the phoneDb is:",""+firstItemPersonalRating);
	firstItemDate = firstItemMap.get(KEY_DATE);
	
	//second item info
	HashMap<String, String> secondItemMap = new HashMap<String, String>();
	db.open();
	secondItemMap = db.getItemData(secondItemName, secondRestaurantName);
	db.close();
	secondItemRestaurant = secondItemMap.get(KEY_RESTAURANT);
	secondItemIngredients = secondItemMap.get(KEY_INGREDIENTS);
	secondItemPrice = secondItemMap.get(KEY_PRICE);
	secondItemCategory = secondItemMap.get(KEY_CATEGORY);
	secondItemTimesOrdered = secondItemMap.get(KEY_TIMESORDERED);
	secondItemPersonalRating = secondItemMap.get(KEY_PERSONALRATING);
	secondItemDate = secondItemMap.get(KEY_DATE);
	}
	
	public void displayInfo(){
		
		//displaying first item info in textviews.
		tv_compResultFirstItemRestaurant.setText(firstItemRestaurant);
		tv_compResultFirstItemIngredients.setText(firstItemIngredients);
		tv_compResultFirstItemPrice.setText(firstItemPrice);
		tv_compResultFirstItemCategory.setText(firstItemCategory);
		tv_compResultFirstItemTimesOrdered.setText(firstItemTimesOrdered);
		if(firstItemTimesOrdered != null){
			if(Integer.parseInt(firstItemPersonalRating) == 1){
				tv_compResultFirstItemPersonalRating.setText(firstItemPersonalRating+" star");
			}else{
				tv_compResultFirstItemPersonalRating.setText(firstItemPersonalRating+" stars");
			}
		}else{
			tv_compResultFirstItemPersonalRating.setText("Not rated yet");
		}
		
		
		tv_compResultFirstItemDate.setText(firstItemDate);
		
		//displaying second item info in textviews.
		tv_compResultSecondItemRestaurant.setText(secondItemRestaurant);
		tv_compResultSecondItemIngredients.setText(secondItemIngredients);
		tv_compResultSecondItemPrice.setText(secondItemPrice);
		tv_compResultSecondItemCategory.setText(secondItemCategory);
		tv_compResultSecondItemTimesOrdered.setText(secondItemTimesOrdered);
		if(secondItemTimesOrdered != null){
		if(Integer.parseInt(secondItemPersonalRating) == 1){
			tv_compResultSecondItemPersonalRating.setText(secondItemPersonalRating+" star");
		}else{
			tv_compResultSecondItemPersonalRating.setText(secondItemPersonalRating+" stars");
		}
		}else{
			tv_compResultSecondItemPersonalRating.setText("Not rated yet");
		}
		tv_compResultSecondItemDate.setText(secondItemDate);
	}
}
