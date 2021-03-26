package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class OrderActivity extends ListActivity{
	// decalring static variables
	public static final String KEY_ITEMNAME = "ItemName";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_QUANTITY = "Quantity";
	public static final String KEY_NO = "No";
	public static final String KEY_WITH = "With";
	public static final String KEY_ADDON = "AddOn";
	public static final String KEY_ADDONPRICE = "AddOnPrice";
	public static final String KEY_TIME = null;
	private static final String TABLE_ORDERS = "orders";
	public static final String KEY_ADDITIONALNOTES = "AdditionalNotes";
	
	// declaring other variables
	ArrayList<HashMap<String,Object>> data;
	ArrayList<String> ItemNameList; 
	
	ListAdapter adapter;
	TextView tvListOrder, backgroundText,OrderTv_modificationWith,OrderTv_modificationNo,OrderTv_addonPrice,OrderTv_itemName,tvMustPressLiveOrder;
	LinearLayout llModificationAndNote;
	Button bClear,bSend;
	
	String itemName, price;
	boolean done ;
	
	//initializing PhoneDb object
	PhoneDb db = new PhoneDb(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_order_list);
		initalize(); 
		getItemNameList();
		
		// gets the order data from the temporary table orders
		db.open();
        data = db.getOrderDataForAdapter();
        db.close();

        // Display the result in a list
        runOnUiThread(new Runnable() {
			public void run() {

				adapter = new SimpleAdapter(
						OrderActivity.this, data,
						R.layout.list_element_order_item,
						new String[] { KEY_ITEMNAME, KEY_PRICE, KEY_NO, KEY_WITH, KEY_ADDON, KEY_ADDONPRICE,KEY_QUANTITY,KEY_ADDITIONALNOTES },
						new int[] { R.id.OrderTv_itemName,R.id.OrderTv_price,R.id.OrderTv_modificationNo,R.id.OrderTv_modificationWith,R.id.OrderTv_addon,R.id.OrderTv_addonPrice, R.id.OrderTv_quantity, R.id.OrderTv_notes }

				);
				setListAdapter(adapter);
				done = true;
			}
		});
        
        
		 ListView lvc = getListView();
         lvc.setCacheColorHint(Color.TRANSPARENT);
         lvc.setClickable(false);
         
         
         if(!data.isEmpty()){
        	 	
        		bClear.setClickable(true);
        		bSend.setClickable(true);
        		lvc.setVisibility(View.VISIBLE);
        		tvMustPressLiveOrder.setVisibility(View.GONE);
        		}
         
	}
	
private void initalize() {
		// TODO initialize variables and Views and sets Font
	bClear = (Button)findViewById(R.id.bClearDb);
	bSend = (Button)findViewById(R.id.bSendOrder);
	backgroundText = (TextView) findViewById (R.id.backgroundText);
	OrderTv_modificationWith = (TextView) findViewById (R.id.OrderTv_modificationWith);
	OrderTv_modificationNo = (TextView) findViewById (R.id.OrderTv_modificationNo);
	OrderTv_addonPrice = (TextView) findViewById (R.id.OrderTv_addonPrice);
	OrderTv_itemName = (TextView) findViewById (R.id.OrderTv_itemName);
	tvMustPressLiveOrder = (TextView) findViewById (R.id.tvMustPressLiveOrder);
	llModificationAndNote = (LinearLayout) findViewById (R.id.llModificationAndNote);
	
	Typeface fontfifa = Typeface.createFromAsset(getAssets(),"fifawelcome.ttf");
	bClear.setTypeface(fontfifa);
	bSend.setTypeface(fontfifa);
	backgroundText.setTypeface(fontfifa);
	tvMustPressLiveOrder.setTypeface(fontfifa);

	}

private void getItemNameList() {
	// TODO Auto-generated method stub
	
	//getting a list containing all item names in the Orders table, only once,
	//to be sent as a parameter for insertOrUpdate();
	
	db.open();
	ItemNameList = db.getItemNameListFromOrders();
	db.close();
}

public void clearOrder(View view){
	
	//clears the ArrayList<HashMap<String,Object>> data used for displaying the list, 
	//and table Orders in the data base
	
	data.clear();
	db.open();
	db.clearTable(TABLE_ORDERS);
	db.close();
	finish();
	startActivity(getIntent());
	
}

public void sendOrder(View view){
	
	//clears the ArrayList<HashMap<String,Object>> data used for displaying the list, 
	//inserts or updates in table History and clears Orders table 
	//prompts for a timer
	
	data.clear();
	db.open();
	for(String itemName: ItemNameList){
		db.insertOrUpdate(itemName);
	}
	db.clearTable(TABLE_ORDERS);
	db.close();
	 Toast OrderSent = Toast.makeText(getApplicationContext(),
 			"Order Sent, Bon Apetit !", Toast.LENGTH_LONG);
     OrderSent.show();
	Intent timerInt = new Intent(getApplicationContext(), WouldYouLikeToStartATimer.class);
    startActivity(timerInt);
	finish();
	
}
	

}
