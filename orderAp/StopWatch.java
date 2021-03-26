package com.application.orderAp;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopWatch extends Activity {

	
	//Declare varianles ********************************************************************************************************************
	public static final String KEY_TIMEPASSED = "TimePassed";
	public static final String KEY_PAUSEDTIME = "PausedTime";
	public static final String KEY_STATUS = "Status";

	Button bReset;
	TextView timer, tv_timeCapture,tvComparingChangingText;
	private Handler mHandler = new Handler();
	private long startTime;
	private long elapsedTime;
	private long timePassed;
	private long pausedTime;
	private final int REFRESH_RATE = 100;
	private String hours, minutes, seconds;
	private long secs, mins, hrs;
	boolean check;
	PhoneDb db;
	String captured = "";

	HashMap<String, Object> map;

	public static final String KEY_FROMLIVEORDER = "fromLiveOrder";
	private static final String TABLE_STOPWATCH = "stopwatch";
	//*******************************************************************************************************************************************
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_stopwatch);
		initialize();
		checkScreenDensity();

		Intent stopwatchInt = getIntent();
		//gets string extra from the previous activity
		int whereFrom = stopwatchInt.getIntExtra(KEY_FROMLIVEORDER, 2);
		db = new PhoneDb(this);
		// the where from varible will stor a number that will be used to identify the activit that started the stopwatch activity
		switch (whereFrom) {
		case 1:
			//it was started from the WouldYouLikeToStartATimer
			startTimer();
			db.open();
			db.clearTable(TABLE_STOPWATCH);
			db.close();
			Log.d("StopWatch/onCreate/the entered case is :", "==== case 1");
			break;
		case 0:
			//it was started from the menu
			db.open();
			check = db.isThisTableEmpty("stopWatch");
			db.close();
			if (check) {
				// if the table stopwatch is empty do nothing
				Log.d("StopWatch/onCreate/the entered case is :",
						"==== case 0 when the table is empty");
				break;
			} else {
				//if the table stopwatch has data we get the info for future calculation
				getSavedPassedTimeAndResume();
				Log.d("StopWatch/onCreate/the entered case is :",
						"==== case 0 when the table has data");
			}
			break;
		}
	}
	private void initialize() {
		// TODO link variables to the widgets and add font
		Typeface fontfifa = Typeface.createFromAsset(getAssets(),
				"fifawelcome.ttf");
		Typeface fontsuperhero = Typeface.createFromAsset(getAssets(),
				"superhero.ttf");
		timer = (TextView) findViewById(R.id.timer);
		timer.setTypeface(fontsuperhero);
		((Button) findViewById(R.id.startButton)).setTypeface(fontfifa);
		bReset = (Button) findViewById(R.id.resetButton);
		bReset.setTypeface(fontfifa);
		((Button) findViewById(R.id.lapButton)).setTypeface(fontfifa);
		tv_timeCapture = (TextView)findViewById(R.id.tv_timeCapture);
		tv_timeCapture.setTypeface(fontsuperhero);
		tvComparingChangingText = (TextView)findViewById(R.id.tvComparingChangingText);
		tvComparingChangingText.setTypeface(fontfifa);
	}
	
	private void checkScreenDensity() {
		//check the screen density to decide whether to incude or not the OrderAp icon
		Button bIcon = (Button) findViewById(R.id.bIcon);
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			bIcon.setVisibility(View.GONE);
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			bIcon.setVisibility(View.GONE);
			break;
		case DisplayMetrics.DENSITY_HIGH:
			bIcon.setVisibility(View.VISIBLE);
			break;
		case DisplayMetrics.DENSITY_TV:
			bIcon.setVisibility(View.VISIBLE);
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			bIcon.setVisibility(View.VISIBLE);
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			bIcon.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private void getSavedPassedTimeAndResume() {
		// TODO Auto-generated method stub
		
		showLapButton();
		map = new HashMap<String, Object>();
		db.open();
		//gets the stopwatch data from map gotten from the phoneDb class
		map = db.getStopWatchData();
		long passedTime = (Long) map.get(KEY_TIMEPASSED);
		Log.d("StopWatch/GetSavedPassedTimeAndResume ( Passed Time )", passedTime +"");
		long pausedTime = (Long) map.get(KEY_PAUSEDTIME);
		Log.d("StopWatch/GetSavedPassedTimeAndResume ( Paused Time )", pausedTime +"");
		db.close();
		//(System.currentTimeMillis() - (pausedTime) = the time elapesed from the moment it was stopped to the currnt time
		long sum = (passedTime) + (System.currentTimeMillis() - (pausedTime));
		Log.d("StopWatch/GetSavedPassedTimeAndResume (sum)", sum +" = " + passedTime + " + " +(System.currentTimeMillis() - (pausedTime)));
		//startTime is the time from which the timer should continue counting
		startTime = System.currentTimeMillis() - sum;
		Log.d("StopWatch/GetSavedPassedTimeAndResume (startTime)", startTime +" = " + System.currentTimeMillis() + " - " + sum);
		mHandler.removeCallbacks(startTimer);
		mHandler.postDelayed(startTimer, 0);
	}

	public void startClick(View view) {
	//the start buton starts the timer
		startTimer();
	}

	private void startTimer() {
		// TODO starts the timer
		showLapButton();
		startTime = System.currentTimeMillis();
		mHandler.removeCallbacks(startTimer);
		mHandler.postDelayed(startTimer, 0);
	}

	public void lapClick(View view) {
		//capture the time displayed and displays it in a textview 
		captured = captured +  timer.getText().toString()+"\n";
		tv_timeCapture.setText(captured);
	}

	public void resetClick(View view) {
	//restets the clock and empty the table stopwatch
		db.open();
		db.clearTable("stopwatch");
		db.close();
		mHandler.removeCallbacks(startTimer);
		timer.setText("00:00:00");
		tv_timeCapture.setText("");
		captured="";
		showStartAndResetButton();
		
	}

	private void showLapButton() {
		((Button) findViewById(R.id.startButton)).setVisibility(View.GONE);
		((Button) findViewById(R.id.lapButton)).setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.resetButton)).setVisibility(View.VISIBLE);
	}
	private void showStartAndResetButton() {
		((Button) findViewById(R.id.startButton)).setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.lapButton)).setVisibility(View.GONE);
		((Button) findViewById(R.id.resetButton)).setVisibility(View.VISIBLE);
	}

	private void updateTimer(float time) {
		secs = (long) (time / 1000);
		mins = (long) ((time / 1000) / 60);
		hrs = (long) (((time / 1000) / 60) / 60);

		/*
		 * Convert the seconds to String and format to ensure it has a leading
		 * zero when required
		 */
		secs = secs % 60;
		seconds = String.valueOf(secs);
		if (secs == 0) {
			seconds = "00";
		}
		if (secs < 10 && secs > 0) {
			seconds = "0" + seconds;
		}

		/* Convert the minutes to String and format the String */

		mins = mins % 60;
		minutes = String.valueOf(mins);
		if (mins == 0) {
			minutes = "00";
		}
		if (mins < 10 && mins > 0) {
			minutes = "0" + minutes;
		}

		/* Convert the hours to String and format the String */

		hours = String.valueOf(hrs);
		if (hrs == 0) {
			hours = "00";
		}
		if (hrs < 10 && hrs > 0) {
			hours = "0" + hours;
		}

		/* Setting the timer text to the elapsed time */
		timer.setText(hours + ":" + minutes + ":" + seconds);
	}

	private Runnable startTimer = new Runnable() {
		public void run() {
			elapsedTime = System.currentTimeMillis() - startTime;
			updateTimer(elapsedTime);
			mHandler.postDelayed(this, REFRESH_RATE);
		}
	};

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//chech if the timer is reset and empty the table stopwatch
		if (timer.getText().toString().equals("00:00:00")) {
			db.open();
			db.clearTable("stopwatch");
			db.close();
			finish();
		} else {
			//if the timer is running save the time passed and time pause to the table stopwatch
			timePassed = System.currentTimeMillis() - startTime;
			pausedTime = System.currentTimeMillis();
			
			Log.d("StopWatch/onPause/timePassed and pausedTime", timePassed + " and " + pausedTime);
			
			db.open();
			db.saveStopwatchData(timePassed, pausedTime);
			db.close();
			finish();
		}

	}
}