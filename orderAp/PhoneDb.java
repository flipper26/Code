package com.application.orderAp;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PhoneDb {

	// To be columns
	public static final String KEY_ITEMNAME = "ItemName";
	public static final String KEY_INGREDIENTS = "Ingredients";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_CATEGORY = "Category";
	public static final String KEY_RESTAURANT = "RestaurantName";
	public static final String KEY_PERSONALRATING = "PersonalRating";
	public static final String KEY_TIMESORDERED = "TimesOrdered";
	public static final String KEY_DATE = "Date";
	public static final String KEY_QUANTITY = "Quantity";
	public static final String KEY_NO = "No";
	public static final String KEY_WITH = "With";
	public static final String KEY_ADDON = "AddOn";
	public static final String KEY_ADDONPRICE = "AddOnPrice";
	public static final String KEY_CUMULATIVEADDONPRICE = "CumulativeAddOnPrice";
	public static final String KEY_CUMULATIVEPRICE = "CumulativePrice";
	public static final String KEY_TIMEPASSED = "TimePassed";
	public static final String KEY_PAUSEDTIME = "PausedTime";
	public static final String KEY_ADDITIONALNOTES = "AdditionalNotes";
	public static final String KEY_TOTALPRICE = "TotalPrice";
	public static final String KEY_TOTALADDONPRICE = "TotalAddOnPrice";
	public static final String KEY_NUMBEROFVISITS = "NumberOfVisits";
	public static final String KEY_RESTAURANTPHONENUMBER = "RestaurantPhoneNumber";

	// Database Name
	private static final String PHONEDB = "PhoneDb";

	// Tables
	private static final String TABLE_ITEMS = "items";
	private static final String TABLE_RESTAURANTS = "restaurants";
	private static final String TABLE_HISTORY = "history";
	private static final String TABLE_MANUALLYADDEDITEMS = "ManuallyAddedItems";
	private static final String TABLE_ORDERS = "orders";
	private static final String TABLE_STOPWATCH = "stopwatch";

	// Database Version
	private static final int DB_VERSION = 1;

	// Instance of the DbHleper class
	private DbHelper ourDbHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		// constructor
		public DbHelper(Context context) {
			super(context, PHONEDB, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		// method used to create the database
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			// Creating the table : items
			db.execSQL("CREATE TABLE " + TABLE_ITEMS + " (" + KEY_ITEMNAME
					+ " TEXT NOT NULL, " + KEY_RESTAURANT + " TEXT NOT NULL, "
					+ KEY_INGREDIENTS + " TEXT NOT NULL, " + KEY_PRICE
					+ " INTEGER, " + KEY_CATEGORY
					+ " TEXT NOT NULL, PRIMARY KEY (" + KEY_ITEMNAME + ", "
					+ KEY_RESTAURANT + "));");
			Log.d(" PhoneDb/DbHelper/onCreate", "items Table Created");

			// Creating table : restaurants
			db.execSQL("CREATE TABLE " + TABLE_RESTAURANTS + " ("
					+ KEY_RESTAURANT + " TEXT NOT NULL, "
					+ KEY_RESTAURANTPHONENUMBER + " TEXT NOT NULL, " + KEY_DATE
					+ " TEXT NOT NULL, " + KEY_NUMBEROFVISITS
					+ " INTEGER, PRIMARY KEY (" + KEY_RESTAURANT + "));");

			// Creating the table : history
			db.execSQL("CREATE TABLE " + TABLE_HISTORY + " (" + KEY_ITEMNAME
					+ " TEXT NOT NULL, " + KEY_TIMESORDERED + " INTEGER, " + KEY_CUMULATIVEPRICE + " INTEGER, "
					+ KEY_RESTAURANT + " TEXT NOT NULL, " + KEY_DATE
					+ " TEXT DEFAULT \'Not ordered yet\', "
					+ KEY_PERSONALRATING + " FLOAT DEFAULT \'0.0\', "
					+ KEY_CUMULATIVEADDONPRICE + " INTEGER, PRIMARY KEY ( " + KEY_ITEMNAME + ", "
					+ KEY_RESTAURANT + "));");
			Log.d(" PhoneDb/DbHelper/onCreate", "history Table Created");

			// Creating the table : ManuallyAddedRecords
			db.execSQL("CREATE TABLE " + TABLE_MANUALLYADDEDITEMS + " ("
					+ KEY_ITEMNAME + " TEXT NOT NULL, " + KEY_RESTAURANT
					+ " TEXT NOT NULL, " + KEY_PRICE
					+ " INTEGER, PRIMARY KEY (" + KEY_ITEMNAME + ", "
					+ KEY_RESTAURANT + "));");
			Log.d(" PhoneDb/DbHelper/onCreate",
					"ManuallyAddedRecords Table Created");

			// Creating the table : orders
			db.execSQL("CREATE TABLE " + TABLE_ORDERS + " (" + KEY_ITEMNAME
					+ " TEXT NOT NULL, " + KEY_RESTAURANT + " TEXT NOT NULL, "
					+ KEY_QUANTITY + " INTEGER, " + KEY_NO + " TEXT, "
					+ KEY_WITH + " TEXT, " + KEY_ADDON + " TEXT, "
					+ KEY_ADDONPRICE + " INTEGER, " + KEY_ADDITIONALNOTES
					+ " TEXT, " + KEY_DATE + " LONG, " + KEY_PRICE
					+ " INTEGER);");
			Log.d(" PhoneDb/DbHelper/onCreate", "orders Table Created");

			// Creating the table : StopWatch
			db.execSQL("CREATE TABLE " + TABLE_STOPWATCH + " ("
					+ KEY_TIMEPASSED + " LONG, " + KEY_PAUSEDTIME + " LONG);");

			Log.d(" PhoneDb/DbHelper/onCreate", "stopwatch Table Created");
		}

		// method used to update the database
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MANUALLYADDEDITEMS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOPWATCH);

			onCreate(db);
		}

	}

	public PhoneDb(Context c) {
		ourContext = c;
	}

	public PhoneDb open() throws SQLiteException {
		ourDbHelper = new DbHelper(ourContext);
		ourDatabase = ourDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourDbHelper.close();

	}

	public long itemsTableEntry(String itemName, String ingredients,
			String category, int price, String restaurantName) {
		// TODO Inserts the items from the server to the phone database's table 'items'
		ContentValues icv = new ContentValues();
		icv.put(KEY_ITEMNAME, itemName);
		icv.put(KEY_INGREDIENTS, ingredients);
		icv.put(KEY_CATEGORY, category);
		icv.put(KEY_PRICE, price);
		icv.put(KEY_RESTAURANT, restaurantName);

		Log.d(" PhoneDb/itemsTableEntry/contentValues Finished",
				"ICV ICV ICV ICV ICV");

		return ourDatabase.insert(TABLE_ITEMS, null, icv);
	}

	public long orderTableEntry(String itemName, String restaurantName,
			int quantity, String no, String with, String addOn, int addOnPrice,
			String date, String price, String additionalNotes) {
		// TODO Insert items to the temporary tabke "orders"

		ContentValues ocv = new ContentValues();
		ocv.put(KEY_ITEMNAME, itemName);
		ocv.put(KEY_RESTAURANT, restaurantName);
		ocv.put(KEY_QUANTITY, quantity);
		ocv.put(KEY_NO, no);
		ocv.put(KEY_WITH, with);
		ocv.put(KEY_ADDON, addOn);
		ocv.put(KEY_ADDONPRICE, addOnPrice);
		ocv.put(KEY_DATE, date);
		ocv.put(KEY_PRICE, price);
		ocv.put(KEY_ADDITIONALNOTES, additionalNotes);

		Log.d("PhoneDb/orderTableEntry/contentValues Finished",
				"OCV OCV OCV OCV OCV");

		return ourDatabase.insert(TABLE_ORDERS, null, ocv);
	}

	public long manuallyAddedItemsTableEntryFromManualAdding(String itemName,
			String restaurantName, String price) {
		// TODO Inserts the manually added items to the table ManuallyAddedItems

		ContentValues hcv = new ContentValues();
		hcv.put(KEY_ITEMNAME, itemName);
		hcv.put(KEY_RESTAURANT, restaurantName);
		hcv.put(KEY_PRICE, price);

		Log.d("PhoneDb/HistoryTableEntryFromManualAdding/contentValues Finished",
				"HCV HCV HCV HCV HCV");

		return ourDatabase.insert(TABLE_MANUALLYADDEDITEMS, null, hcv);

	}
	
	public String getPhoneNumber(String restaurantName) {
		// TODO gets the phone Number from the server and return it to the SavedCategories Activity
		String phoneNumber;
		String[] columns = new String[] { KEY_RESTAURANTPHONENUMBER };
		Cursor c = ourDatabase.query(TABLE_RESTAURANTS, columns,"RestaurantName = ?", new String[]{ restaurantName }, null,
				null, null);
		int iRestaurantPhoneNumber = c.getColumnIndex(KEY_RESTAURANTPHONENUMBER);
		c.moveToFirst();
		phoneNumber = c.getString(iRestaurantPhoneNumber);
		return phoneNumber;
	}


	public ArrayList<HashMap<String, Object>> getOrderDataForAdapter() {
		// TODO Gets the data from the table orders and return it in a list to show it in the orderActivity
		String[] columns = new String[] { KEY_ITEMNAME, KEY_RESTAURANT,
				KEY_QUANTITY, KEY_NO, KEY_WITH, KEY_ADDON, KEY_ADDONPRICE,
				KEY_DATE, KEY_PRICE, KEY_ADDITIONALNOTES };
		ArrayList<HashMap<String, Object>> orderData = new ArrayList<HashMap<String, Object>>();
		Cursor c = ourDatabase.query(TABLE_ORDERS, columns, null, null, null,
				null, null);
		int price = 0;
		int quantity = 1;
		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		int iQuantity = c.getColumnIndex(KEY_QUANTITY);
		int iNo = c.getColumnIndex(KEY_NO);
		int iWith = c.getColumnIndex(KEY_WITH);
		int iAddOn = c.getColumnIndex(KEY_ADDON);
		int iAddOnPrice = c.getColumnIndex(KEY_ADDONPRICE);
		int iDate = c.getColumnIndex(KEY_DATE);
		int iPrice = c.getColumnIndex(KEY_PRICE);
		int iNotes = c.getColumnIndex(KEY_ADDITIONALNOTES);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			price = c.getInt(iPrice);
			quantity = c.getInt(iQuantity);
			Log.d("PhoneDb/getOrderData", price + " " + quantity);
			int quantityByPrice = price * quantity;
			map.put(KEY_ITEMNAME, c.getString(iItemName));
			map.put(KEY_QUANTITY, c.getString(iQuantity));
			map.put(KEY_NO, c.getString(iNo));
			map.put(KEY_WITH, c.getString(iWith));
			map.put(KEY_ADDON, c.getString(iAddOn));
			map.put(KEY_DATE, c.getString(iDate));
			map.put(KEY_PRICE, quantityByPrice + " L.L.");
			map.put(KEY_ADDONPRICE, c.getString(iAddOnPrice) + " L.L.");
			map.put(KEY_ADDITIONALNOTES,"Note: "+c.getString(iNotes));

			orderData.add(map);
		}
		return orderData;
	}

	public ArrayList<HashMap<String, String>> getSavedRestaurantName() {

		// TODO search for visited restaurants name in the table restaurants 
		// and return them in a list of maps to the SavedRestaurantNames Activity
		
		ArrayList<HashMap<String, String>> restaurantsList = new ArrayList<HashMap<String, String>>();
		String[] columns = new String[] { KEY_RESTAURANT };
		Cursor c = ourDatabase.query(TABLE_RESTAURANTS, columns, null, null,
				KEY_RESTAURANT, null, null);
		String result = "";
		String dResult = "";

		int restaurantName = c.getColumnIndex(KEY_RESTAURANT);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = c.getString(restaurantName);

			String[] column = new String[] { KEY_DATE };
			Cursor cur = ourDatabase.query(TABLE_RESTAURANTS, column,
					"RestaurantName=?", new String[] { result },
					KEY_RESTAURANT, null, null);
			int idate = cur.getColumnIndex(KEY_DATE);
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				dResult = cur.getString(idate);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(KEY_RESTAURANT, result);
				map.put(KEY_DATE, dResult);

				Log.d("phoneDb/GetSavedRestaurantName/dResult", result
						+ dResult);
				restaurantsList.add(map);
			}
		}

		return restaurantsList;
	}

	public int getAddOnPrice(String itemName) {

		// TODO Returns the addOn price as an int of a particular itemName
		int price = 0;
		String[] columns = new String[] { KEY_PRICE };
		Cursor c = ourDatabase.query(TABLE_ITEMS, columns, "ItemName=?",
				new String[] { itemName }, null, null, null);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			int iPrice = c.getColumnIndex(KEY_PRICE);

			price = c.getInt(iPrice);
			Log.d(" getAddOnPrice price", price + " L.L.");
		}
		return price;
	}

	public ArrayList<HashMap<String, String>> getSavedCategories(
			String restaurantName) {

		// TODO  Gets the saved categories for a specific restaurantName 
		//and return the categories in a list of maps for adapters in savedCategories activity
		ArrayList<HashMap<String, String>> categoriesList = new ArrayList<HashMap<String, String>>();

		String[] columns = new String[] { KEY_CATEGORY };
		Cursor c = ourDatabase
				.query(TABLE_ITEMS,
						columns,
						"RestaurantName=?",
						new String[] { restaurantName },
						KEY_CATEGORY,
						"Category <> 'Add on' and Category <> 'With' and Category <> 'No'",
						null);
		String result;

		int iCategory = c.getColumnIndex(KEY_CATEGORY);
		Log.d(" getSavedCategories cursor", c.toString());
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = c.getString(iCategory);
			Log.d(" getSavedCategories result", result.toString());

			HashMap<String, String> map = new HashMap<String, String>();
			map.put(KEY_CATEGORY, result);

			categoriesList.add(map);
		}

		Log.d(" getSavedCategories categoriesList", categoriesList.toString());

		return categoriesList;
	}

	public ArrayList<HashMap<String, String>> getItemsInfo(String category,
			String restaurant) {
		// TODO resturns item name ingrediants and price in an array list of maps 
		//for a specific restaurant and category for the adapter in SavedItems activity
		String[] columns = new String[] { KEY_ITEMNAME, KEY_INGREDIENTS,
				KEY_PRICE };
		Cursor c = ourDatabase.query(TABLE_ITEMS, columns,
				"Category = ? and RestaurantName = ?", new String[] { category,
						restaurant }, null, null, null);

		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		int iIngredients = c.getColumnIndex(KEY_INGREDIENTS);
		int iPrice = c.getColumnIndex(KEY_PRICE);

		ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String, String>>();

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String itemName = c.getString(iItemName);
			String ingredients = c.getString(iIngredients);
			String price = c.getString(iPrice);

			HashMap<String, String> map = new HashMap<String, String>();

			map.put(KEY_ITEMNAME, itemName);
			map.put(KEY_INGREDIENTS, ingredients);
			map.put(KEY_PRICE, price);

			itemsList.add(map);
		}
		return itemsList;

	}

	public String[] getListForSpinners(String restaurant,
			String modificationType, int a) {
		// TODO returns a string array of all the items in a specific modification (catrgory) type and
		//is displayed according to the third variable which can be either 1 or anything else used in the liveOrderItem activity

		String[] columns = new String[] { KEY_ITEMNAME, KEY_PRICE };
		Cursor c = ourDatabase.query(TABLE_ITEMS, columns,
				"RestaurantName = ? and Category = ?", new String[] {
						restaurant, modificationType }, null, null, null);

		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		int iModificationPrice = c.getColumnIndex(KEY_PRICE);
		ArrayList<String> list = new ArrayList<String>();
		list.add(modificationType);
		if (a == 1) {

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				String itemName = c.getString(iItemName);
				String AddOnPrice = c.getString(iModificationPrice);
				list.add(itemName + " - " + AddOnPrice + " L.L.  ");
			}
		} else {

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				String itemName = c.getString(iItemName);
				list.add(itemName);
			}

		}
		Log.d("List returned from spinners", list.toString());
		String[] stringArray = new String[list.size()];
		stringArray = list.toArray(stringArray);
		return stringArray;

	}

	public String getDate(String restaurantName) {
		// TODO resturns the saved date for a specific restaurant and 
		//"No Date" if the restaurnt was never visited before used in the liveOrederCategoryMenu activity
		String savedDate = "";
		String[] columns = new String[] { KEY_DATE };
		Cursor cur = ourDatabase.rawQuery("SELECT COUNT(*) FROM "
				+ TABLE_RESTAURANTS, null);
		if (cur != null) { // always True
			cur.moveToFirst();
			if (cur.getInt(0) == 0) { // table is empty
				savedDate = "No Date";
			} else {
				Cursor c = ourDatabase.query(TABLE_RESTAURANTS, columns,
						"RestaurantName=?", new String[] { restaurantName },
						KEY_DATE, null, null);

				int iDate = c.getColumnIndex(KEY_DATE);
				Log.d("phoneDb/getDate(cursor)", c.toString());

				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					savedDate = c.getString(iDate);
					Log.d("phoneDb/getDate(SavedDate)", c.toString());
				}
			}
		}

		return savedDate;
	}

	public void clearTable(String tableName) {
		//Used to clear a table given it's name
		ourDatabase.delete(tableName, null, null);
	}

	public String[] getListOfRestaurants() {
		// TODO returns a string array of all the restaurants in the table items to the activity comparing

		String[] columns = new String[] { KEY_RESTAURANT };
		Cursor c = ourDatabase.query(TABLE_ITEMS, columns, null, null,
				"RestaurantName", null, null);

		int iRestName = c.getColumnIndex(KEY_RESTAURANT);
		ArrayList<String> list = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String restName = c.getString(iRestName);
			list.add(restName);
		}
		Log.d("List of restaurants names returned for edit texts",
				list.toString());
		String[] stringArray = new String[list.size()];
		stringArray = list.toArray(stringArray);
		return stringArray;
	}

	public String[] getListOfItems(String selectedRest) {
		//returns a string array of all the items in the table items for a given restaurant to the activity comparing
		String[] columns = new String[] { KEY_ITEMNAME };
		Cursor c = ourDatabase
				.query(TABLE_ITEMS,
						columns,
						"RestaurantName = ?",
						new String[] { selectedRest },
						"ItemName",
						"Category <> 'Add on' and Category <> 'With' and Category <> 'No'",
						null);
		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		ArrayList<String> list = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String itemName = c.getString(iItemName);
			list.add(itemName);
		}
		Log.d("List of itemNames returned foe edit texts", list.toString());
		String[] stringArray = new String[list.size()];
		stringArray = list.toArray(stringArray);
		return stringArray;
	}

	public Boolean isThisTableEmpty(String tableName) {
		//given the table name it checkes if the table is empty
		String check = null;
		Cursor cur = ourDatabase.rawQuery("SELECT COUNT(*) FROM " + tableName
				+ "", null);
		if (cur != null) {
			cur.moveToFirst(); // Always one row returned.
			if (cur.getInt(0) == 0) { // Zero count means empty table.
				check = "true";
				Log.d("PhoneDb/ isThisTableEmpty:", check);
				return true;
			} else {
				check = "false";
				Log.d("PhoneDb/ isThisTableEmpty:", check);
				return false;
			}

		}
		Log.d("PhoneDb/ isThisTableEmpty:", check);
		return null;

	}

	public Boolean isThisRowEmpty(String tableName, String selection) {
		//given the table name and a selection it checkes if the row is empty
		String check = null;
		Cursor cur = ourDatabase.rawQuery("SELECT COUNT(*) FROM " + tableName
				+ " WHERE ItemName = ?", new String[] { selection });
		if (cur != null) {
			cur.moveToFirst(); // Always one row returned.
			if (cur.getInt(0) == 0) { // Zero count means empty row.
				check = "true";
				Log.d("PhoneDb/ isThisRowEmpty:", check);
				return true;
			} else {
				check = "false";
				Log.d("PhoneDb/ isThisRowEmpty:", check);
				return false;
			}

		}
		Log.d("PhoneDb/ isThisRowEmpty:", check);
		return null;

	}

	public long saveStopwatchData(long timePassed, long pausedTime) {
		// TODO used to save to the table stopwatch the time passed and paused time in the activity Stopwatch

		ContentValues tcv = new ContentValues();
		tcv.put(KEY_TIMEPASSED, timePassed);
		tcv.put(KEY_PAUSEDTIME, pausedTime);

		Log.d("PhoneDb/saveStopwatchData/timePassed and pausedTime", timePassed
				+ " and " + pausedTime);
		Log.d("PhoneDb/saveStopwatchData/contentValues Finished",
				"TCV TCV TCV TCV TCV");

		return ourDatabase.insert(TABLE_STOPWATCH, null, tcv);
	}

	public HashMap<String, Object> getStopWatchData() {
		// TODO returns in a map the stopwatch data to the activity stopwatch
		HashMap<String, Object> map = new HashMap<String, Object>();
		String[] columns = new String[] { KEY_TIMEPASSED, KEY_PAUSEDTIME };
		Cursor c = ourDatabase.query(TABLE_STOPWATCH, columns, null, null,
				null, null, null);

		int iTimePassed = c.getColumnIndex(KEY_TIMEPASSED);
		int iPausedTime = c.getColumnIndex(KEY_PAUSEDTIME);

		Log.d("phoneDb/pausedTime(cursor)", c.toString());

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			map.put(KEY_TIMEPASSED, c.getLong(iTimePassed));
			map.put(KEY_PAUSEDTIME, c.getLong(iPausedTime));

			Log.d("phoneDb/pausedTime(savedPassedTime)", c.getLong(iTimePassed)
					+ "");
			Log.d("phoneDb/pausedTime(iPausedTime)", c.getLong(iPausedTime)
					+ "");
		}

		return map;
	}

	public HashMap<String, String> getItemData(String itemName,
			String restaurantName) {
		// TODO returns in a map, restaurantName, Ingredients, price, category from the table 'items'
		// and timesOrdered, PersonalRating, Date from the table 'history' to the activity Comparing result
		String[] itemsColumns = new String[] { KEY_RESTAURANT, KEY_INGREDIENTS,
				KEY_PRICE, KEY_CATEGORY };
		Cursor ic = ourDatabase.query(TABLE_ITEMS, itemsColumns,
				"ItemName = ?  and RestaurantName = ? ", new String[] {
						itemName, restaurantName }, null, null, null);
		HashMap<String, String> map = new HashMap<String, String>();

		int iRestaurant = ic.getColumnIndex(KEY_RESTAURANT);
		int iIngredients = ic.getColumnIndex(KEY_INGREDIENTS);
		int iPrice = ic.getColumnIndex(KEY_PRICE);
		int iCategory = ic.getColumnIndex(KEY_CATEGORY);

		for (ic.moveToFirst(); !ic.isAfterLast(); ic.moveToNext()) {

			map.put(KEY_RESTAURANT, ic.getString(iRestaurant));
			map.put(KEY_INGREDIENTS, ic.getString(iIngredients));
			map.put(KEY_PRICE, ic.getString(iPrice));
			map.put(KEY_CATEGORY, ic.getString(iCategory));
		}

		String[] historyColumns = new String[] { KEY_TIMESORDERED,
				KEY_PERSONALRATING, KEY_DATE };
		Cursor hc = ourDatabase.query(TABLE_HISTORY, historyColumns,
				"ItemName = ?  and RestaurantName = ? ", new String[] {
						itemName, restaurantName }, null, null, null);

		int iTimesOrdered = hc.getColumnIndex(KEY_TIMESORDERED);
		int iPersonalRating = hc.getColumnIndex(KEY_PERSONALRATING);
		int iDate = hc.getColumnIndex(KEY_DATE);

		for (hc.moveToFirst(); !hc.isAfterLast(); hc.moveToNext()) {

			map.put(KEY_TIMESORDERED, hc.getString(iTimesOrdered));
			map.put(KEY_PERSONALRATING, hc.getString(iPersonalRating));
			map.put(KEY_DATE, hc.getString(iDate));

			Log.d("phoneDb/getItemData(map)", map.toString());
		}
		return map;

	}

	public long getOrderDataAndSaveToHistory(String orderItemName) {
		// TODO Auto-generated method stub
		String itemName, restaurantName, date;
		int sumOfQuantities = 0;
		int sumOfAddOns = 0;
		int sumOfPrice = 0;
		// Getting the sum of quantities and addOnPrice from the table orders
		Cursor cQuantityAndAddOn = ourDatabase.rawQuery(
				"SELECT SUM(Quantity), SUM(AddOnPrice) FROM Orders WHERE ItemName = ?",
				new String[] { orderItemName });

		Log.d(" PhoneDb/getOrderDataAndSaveToHistory/orderItemName ",
				orderItemName.toString());
		
		int iSumOfQuantities = cQuantityAndAddOn.getColumnIndexOrThrow("SUM(Quantity)");
		int iSumOfAddOns = cQuantityAndAddOn.getColumnIndex("SUM(AddOnPrice)");
		
		 if(cQuantityAndAddOn.moveToFirst()){
			sumOfQuantities = cQuantityAndAddOn.getInt(iSumOfQuantities);
			sumOfAddOns = cQuantityAndAddOn.getInt(iSumOfAddOns);

			Log.d(" PhoneDb/getOrderDataAndSaveToHistory/sumOfQuantities and sumOfAddOns(cursor not empty)",
					sumOfQuantities + " " + sumOfAddOns);
		}else{
			Log.d(" PhoneDb/getOrderDataAndSaveToHistory","cursor is empty");
		}
		Cursor cCumPrice = ourDatabase.rawQuery(
				"SELECT SUM(Quantity * Price) FROM Orders WHERE ItemName = ?",
				new String[] { orderItemName });
		 if(cCumPrice.moveToFirst()){
			 sumOfPrice = cCumPrice.getInt(0);
		 }
		 Log.d(" PhoneDb/getOrderDataAndSaveToHistory/sumOfPrice(cursor not empty)",
				 sumOfPrice+"");

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

		// Getting ItemName, RestaurantName, Date from Orders
		String[] columns = new String[] { KEY_ITEMNAME, KEY_RESTAURANT,
				KEY_DATE };
		Cursor c = ourDatabase.query(TABLE_ORDERS, columns, "ItemName = ?",
				new String[] { orderItemName }, null, null, null);
		c.moveToFirst();
		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		int iRestaurantName = c.getColumnIndex(KEY_RESTAURANT);
		int iDate = c.getColumnIndex(KEY_DATE);

		itemName = c.getString(iItemName);
		restaurantName = c.getString(iRestaurantName);
		date = c.getString(iDate);

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

		// inserting the data to the table History
		ContentValues hcv = new ContentValues();

		hcv.put(KEY_DATE, date);
		hcv.put(KEY_ITEMNAME, itemName);
		hcv.put(KEY_RESTAURANT, restaurantName);
		hcv.put(KEY_TIMESORDERED, sumOfQuantities);
		hcv.put(KEY_CUMULATIVEADDONPRICE, sumOfAddOns);
		hcv.put(KEY_CUMULATIVEPRICE, sumOfPrice);

		Log.d(" PhoneDb/getOrderDataAndSaveToHistory/sumOfQuantities and AddOns:",
				sumOfQuantities + " " + sumOfAddOns);
		Log.d(" PhoneDb/getOrderDataAndSaveToHistory/contentValues Finished",
				hcv.toString());
		return ourDatabase.insert(TABLE_HISTORY, null, hcv);
	}

	private long getOrderDataAndUpdateHistory(String itemName) {
		// TODO Auto-generated method stub

		int TimesOrdered = 0;
		int sumOfQuantities = 0;
		int sumOfAddOnsSaved = 0;
		int sumOfPriceSaved = 0;
		int sumOfPrice = 0;
		int sumOfAddOns = 0;
		int totalTimesOrdered = 0;
		int totalAddOnPrice = 0;
		int totalCumulativePrice = 0;
		String item_Name, restaurant_Name, date;

		// Getting the sum of quantities and AddOnPrice from the table History
		Cursor histC = ourDatabase.rawQuery(
				"SELECT TimesOrdered, CumulativeAddOnPrice, CumulativePrice  FROM History WHERE ItemName = ?",
				new String[] { itemName });
		
		int iTimesOrdereds = histC.getColumnIndex(KEY_TIMESORDERED);
		int iSumOfAddOnsSaved = histC.getColumnIndex(KEY_CUMULATIVEADDONPRICE);
		int iSumOfPriceSaved = histC.getColumnIndex(KEY_CUMULATIVEPRICE);
		
		for (histC.moveToFirst(); !histC.isAfterLast(); histC.moveToNext()) {
			TimesOrdered = histC.getInt(iTimesOrdereds);
			sumOfAddOnsSaved = histC.getInt(iSumOfAddOnsSaved);
			sumOfPriceSaved = histC.getInt(iSumOfPriceSaved);

			Log.d(" PhoneDb/getOrderDataAndUpdateHistory/TimesOrdered, SumOfAddOnsSaved, sumOfPriceSaved (cursor not empty)",
					TimesOrdered + " " + sumOfAddOnsSaved + " " + sumOfPriceSaved );
		}

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		
		//Getting the sum of quantities from the table Orders
		Cursor cQuantityAndAddOn = ourDatabase.rawQuery(
				"SELECT SUM(Quantity), SUM(AddOnPrice) FROM Orders WHERE ItemName = ?",
				new String[] {itemName});

		Log.d(" PhoneDb/getOrderDataAndSaveToHistory/orderItemName ",
				itemName.toString());
		
		int iSumOfQuantities = cQuantityAndAddOn.getColumnIndex("SUM(Quantity)");
		int iSumOfAddOns = cQuantityAndAddOn.getColumnIndex("SUM(AddOnPrice)");
		
		for (cQuantityAndAddOn.moveToFirst(); !cQuantityAndAddOn.isAfterLast(); cQuantityAndAddOn.moveToNext()) {
			sumOfQuantities = cQuantityAndAddOn.getInt(iSumOfQuantities);
			sumOfAddOns = cQuantityAndAddOn.getInt(iSumOfAddOns);
		}
			Log.d(" PhoneDb/getOrderDataAndSaveToHistory/sumOfQuantities and sumOfAddOns(cursor not empty)",
					sumOfQuantities + " " + sumOfAddOns);
			
			Cursor cCumPrice = ourDatabase.rawQuery(
					"SELECT SUM(Quantity * Price) FROM Orders WHERE ItemName = ?",
					new String[] { itemName });
			 if(cCumPrice.moveToFirst()){
				 sumOfPrice = cCumPrice.getInt(0);
			 }
			 Log.d(" PhoneDb/getOrderDataAndSaveToHistory/sumOfPrice(cursor not empty)",
					 sumOfPrice + "");
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

		// getting how many times an item has been ordered
		totalTimesOrdered = TimesOrdered + sumOfQuantities;
		totalAddOnPrice = sumOfAddOnsSaved + sumOfAddOns;
		totalCumulativePrice = sumOfPriceSaved + sumOfPrice;

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

		// Getting the rest of the data from Orders
		String[] columns = new String[] { KEY_ITEMNAME, KEY_RESTAURANT,
				KEY_DATE };
		Cursor c = ourDatabase.query(TABLE_ORDERS, columns, "ItemName = ?",
				new String[] { itemName }, null, null, null);
		c.moveToFirst();
		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		int iRestaurantName = c.getColumnIndex(KEY_RESTAURANT);
		int iDate = c.getColumnIndex(KEY_DATE);

		item_Name = c.getString(iItemName);
		restaurant_Name = c.getString(iRestaurantName);
		date = c.getString(iDate);

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

		// updating the data to the table History
		ContentValues hcv = new ContentValues();

		hcv.put(KEY_DATE, date);
		hcv.put(KEY_ITEMNAME, item_Name);
		hcv.put(KEY_RESTAURANT, restaurant_Name);
		hcv.put(KEY_TIMESORDERED, totalTimesOrdered);
		hcv.put(KEY_CUMULATIVEADDONPRICE, totalAddOnPrice);
		hcv.put(KEY_CUMULATIVEPRICE, totalCumulativePrice);

		Log.d(" PhoneDb/getOrderDataAndUpdateHistory/totalTimesOrdered:",
				totalTimesOrdered + "");
		Log.d(" PhoneDb/getOrderDataAndUpdateHistory/contentValues Finished",
				hcv.toString());
		
		return ourDatabase.update(TABLE_HISTORY, hcv, "ItemName = ?",
				new String[] { itemName });
		}

	public void insertOrUpdate(String itemName) {
		//if the row with the selected itemName do not exist this function calls getOrderDataAndSaveToHistory(String itemName), 
		//if it exists it calls getOrderDataAndUpdateHistory(String itemName)
		Cursor cur = ourDatabase.rawQuery(
				"SELECT COUNT(*) FROM history WHERE ItemName = ?",
				new String[] { itemName });
		if (cur != null) {
			cur.moveToFirst(); // Always one row returned.
			if (cur.getInt(0) == 0) { // Zero count means empty table.
				// insert a new row .
				Log.d("PhoneDb/insertOrUpdate :", "INSERT WAS SELECTED");
				getOrderDataAndSaveToHistory(itemName);
				
			} else { // this item exists so we have to update this row and add
				// the quantity and replace the date.
				Log.d("PhoneDb/ insertOrUpdate:", "Update was selected");
				getOrderDataAndUpdateHistory(itemName);
				
			}
		}
	}

	public ArrayList<String> getItemNameListFromOrders() {
		// TODO Returns an arrayList of all the itemNames in the table orderes to the activity OrderActivity
		String[] columns = new String[] { KEY_ITEMNAME };
		Cursor c = ourDatabase.query(TABLE_ORDERS, columns, null, null,
				"ItemName", null, null);

		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		ArrayList<String> list = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String itemName = c.getString(iItemName);
			list.add(itemName);
		}
		Log.d("PhoneDb/getItemNameListFromOrders/List Of ItemNames",
				list.toString());
		return list;
	}

	public long setPersonalRating(String itemName, int ratingNum) {
		// TODO Given an item name and an integer in the activity history 
		//this function will save the int as a rating for the specific item
		String item_Name, restaurant_Name, date;
		int timesordered;
		String[] columns = new String[] { KEY_ITEMNAME, KEY_RESTAURANT,
				KEY_DATE, KEY_TIMESORDERED, KEY_PERSONALRATING };
		Cursor c = ourDatabase.query(TABLE_HISTORY, columns, "ItemName = ?",
				new String[] { itemName }, null, null, null);
		c.moveToFirst();
		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		int iRestaurantName = c.getColumnIndex(KEY_RESTAURANT);
		int iDate = c.getColumnIndex(KEY_DATE);
		int iTimesOrdered = c.getColumnIndex(KEY_TIMESORDERED);

		item_Name = c.getString(iItemName);
		restaurant_Name = c.getString(iRestaurantName);
		date = c.getString(iDate);
		timesordered = c.getInt(iTimesOrdered);

		ContentValues hcv = new ContentValues();

		hcv.put(KEY_ITEMNAME, item_Name);
		hcv.put(KEY_RESTAURANT, restaurant_Name);
		hcv.put(KEY_DATE, date);
		hcv.put(KEY_TIMESORDERED, timesordered);
		hcv.put(KEY_PERSONALRATING, ratingNum);

		Log.d(" PhoneDb/setPersonnalRating, the rating to set is:",
				String.valueOf(ratingNum));
		Log.d(" PhoneDb/getOrderDataAndUpdateHistory/contentValues Finished",
				hcv.toString());

		return ourDatabase.update(TABLE_HISTORY, hcv, "ItemName = ?",
				new String[] { itemName });
	}

	public int getPersonalRating(String itemName) {
		//Returns the personal rating saved in table history as an int to the activity History
		int personalRating;
		String[] columns = new String[] { KEY_PERSONALRATING };
		Cursor c = ourDatabase.query(TABLE_HISTORY, columns, "ItemName = ?",
				new String[] { itemName }, null, null, null);
		c.moveToFirst();
		int iPersonalRating = c.getColumnIndex(KEY_PERSONALRATING);
		personalRating = c.getInt(iPersonalRating);
		Log.d("PhoneDb/ getPersonalRating returned:",
				String.valueOf(personalRating));
		return personalRating;

	}

	public String[] getRestaurantsfromHistory() {
		// TODO returns all the restaurants saved in the table history in a string array to the activity ExpenseCalculating

		String[] columns = new String[] { KEY_RESTAURANT };
		Cursor c = ourDatabase.query(TABLE_HISTORY, columns, null, null,
				"RestaurantName", null, null);

		int iRestName = c.getColumnIndex(KEY_RESTAURANT);
		ArrayList<String> list = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			String restName = c.getString(iRestName);
			list.add(restName);
		}
		Log.d("PhoneDb/ List of restaurants names returned for edit texts",
				list.toString());
		String[] stringArray = new String[list.size()];
		stringArray = list.toArray(stringArray);
		return stringArray;
	}

	public long insertRestaurantNameAndAddCount(String restaurantName, String restaurantPhoneNumber, 
			String todayDate) {
		// TODO used from the LiveOrderCategoryMenu to save the restaurant name the last visit date, 
		//the phone number and the number of visits to the table restaurant
		ContentValues cv = new ContentValues();

		cv.put(KEY_RESTAURANT, restaurantName);
		cv.put(KEY_DATE, todayDate);
		cv.put(KEY_RESTAURANTPHONENUMBER, restaurantPhoneNumber);
		cv.put(KEY_NUMBEROFVISITS, 1);
		return ourDatabase.insert(TABLE_RESTAURANTS, null, cv);
	}

	public long updateNumberOfVisitsAndDate(String restaurantName, String restaurantPhoneNumber, 
			String todayDate) {
		// TODO used from the LiveOrderCategoryMenu to update the restaurant name the last visit date, 
		//the phone number and the number of visits in the table restaurant
		String[] columns = new String[] { KEY_NUMBEROFVISITS };
		Cursor c = ourDatabase.query(TABLE_RESTAURANTS, columns,
				"RestaurantName = ?", new String[] { restaurantName }, null,
				null, null);

		int iNumberOfVisits = c.getColumnIndex(KEY_NUMBEROFVISITS);

		c.moveToFirst();
		int numberOfVisits = c.getInt(iNumberOfVisits);

		ContentValues cv = new ContentValues();

		cv.put(KEY_RESTAURANT, restaurantName);
		cv.put(KEY_RESTAURANTPHONENUMBER, restaurantPhoneNumber);
		cv.put(KEY_DATE, todayDate);
		cv.put(KEY_NUMBEROFVISITS, numberOfVisits + 1);

		return ourDatabase.update(TABLE_RESTAURANTS, cv, "RestaurantName = ?",
				new String[] { restaurantName });

	}


	public int calculateOneRestaurantExpense(String selectedRestaurant) {
		// TODO returns a integer value of all the expenses spent in one restaurant to the activity ExpenseCalculating
		int cumulativePrice, cumulativeAddOnPrice;
		Cursor c = ourDatabase.rawQuery(
				"SELECT SUM(CumulativePrice), SUM(CumulativeAddOnPrice) FROM History WHERE RestaurantName = ?",
				new String[] { selectedRestaurant });
		c.moveToFirst();
		int iCumulativePrice = c.getColumnIndex("SUM(CumulativePrice)");
		int iCumulativeAddOnPrice = c.getColumnIndex("SUM(CumulativeAddOnPrice)");
		
		cumulativePrice = c.getInt(iCumulativePrice);
		cumulativeAddOnPrice = c.getInt(iCumulativeAddOnPrice);
		
		int totoalOneRestaurantExpense = cumulativePrice + cumulativeAddOnPrice;
		
		Log.d("PhoneDb/calculateOneRestaurantExpense/totoalOneRestaurantExpense",totoalOneRestaurantExpense +"");
		return totoalOneRestaurantExpense;
	}

	public int calculateAllRestaurantsExpense() {
		// TODO returns a integer value of all the expenses spent in all restaurants to the activity ExpenseCalculating
		int cumulativePrice, cumulativeAddOnPrice;
		Cursor c = ourDatabase.rawQuery(
				"SELECT SUM(CumulativePrice), SUM(CumulativeAddOnPrice) FROM History",null);
		c.moveToFirst();
		int iCumulativePrice = c.getColumnIndex("SUM(CumulativePrice)");
		int iCumulativeAddOnPrice = c.getColumnIndex("SUM(CumulativeAddOnPrice)");
		
		cumulativePrice = c.getInt(iCumulativePrice);
		cumulativeAddOnPrice = c.getInt(iCumulativeAddOnPrice);
		
		int totalAllRestaurantExpense = cumulativePrice + cumulativeAddOnPrice;
		
		Log.d("PhoneDb/calculateAllRestaurantExpense/totalAllRestaurantExpense",totalAllRestaurantExpense +"");
		return totalAllRestaurantExpense;
	}

	public int calculateManuallyAddedItemsExpense() {
		// TODO returns a integer value of all the expenses spent on manually added items to the activity ExpenseCalculating
		Cursor c = ourDatabase.rawQuery(
				"SELECT SUM(Price) FROM ManuallyAddedItems",null);
		c.moveToFirst();
		int cumulativeManualPrice = c.getInt(0);
		
		Log.d("PhoneDb/calculateManuallyAddedItemsExpense/cumulativeManualPrice",cumulativeManualPrice +"");
		return cumulativeManualPrice;
	}

	public ArrayList<HashMap<String, Object>> getManuallyaddedItemsForAdapter() {
		// TODO Returns a Array list of maps of the restaurantName Itemname and price 
		//for the adapter in the activity ManuallyAddedItemsList
		ArrayList<HashMap<String, Object>> orderData = new ArrayList<HashMap<String, Object>>();
		String[] columns = new String[] { KEY_ITEMNAME, KEY_RESTAURANT,KEY_PRICE};

		Cursor c = ourDatabase.query(TABLE_MANUALLYADDEDITEMS, columns, null, null, null,
				null, null);

		int iRestaurant = c.getColumnIndex(KEY_RESTAURANT);
		int iItemName = c.getColumnIndex(KEY_ITEMNAME);
		int iPrice = c.getColumnIndex(KEY_PRICE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put(KEY_RESTAURANT, c.getString(iRestaurant));
			map.put(KEY_ITEMNAME, c.getString(iItemName));
			map.put(KEY_PRICE, c.getInt(iPrice));
			
			orderData.add(map);
		}
		return orderData;
	}

	public String getNumberOfVisits(String restaurantName) {
		// TODO returns the number of visits given a specific restaurant used in the SavedCategory activity
		String numberOfVisits;
		String[] columns = new String[] { KEY_NUMBEROFVISITS};
		Cursor c = ourDatabase.query(TABLE_RESTAURANTS, columns, "RestaurantName = ?", new String[]{restaurantName}, null,
				null, null);
		int iNumberOfVisits = c.getColumnIndex(KEY_NUMBEROFVISITS);
		c.moveToFirst();
		numberOfVisits = c.getString(iNumberOfVisits);
		return numberOfVisits;
	}
}
