package com.application.orderAp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;


public class Splash extends Activity {

	Intent menuStarter;
	Thread timer;
	Boolean shouldcontinue = true;
	TextView tvOrderAp, tvService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);
		//initialize and add font
		Typeface fontfifa = Typeface.createFromAsset(getAssets(), "fifawelcome.ttf");  
		tvOrderAp = (TextView) findViewById(R.id.tvOrderAp);
		tvOrderAp.setTypeface(fontfifa);
		tvService = (TextView) findViewById(R.id.tvService);
		tvService.setTypeface(fontfifa);
		
		//start a thread that wil sleep for 3.5 seconds
		timer = new Thread() {
			public void run() {
				try {
					sleep(3500);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					//once the sleep period is over if the back button was not pressed the activity OrderApMenu starts
					if (shouldcontinue) {
						menuStarter = new Intent(Splash.this, OrderApMenu.class);
						startActivity(menuStarter);
					}
				}
			}
		};
		timer.start();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	public void onBackPressed() {
		//sets the bool value to false and starts the activity to avoid starting OrderApMenu twice
		shouldcontinue = false;
		menuStarter = new Intent(Splash.this, OrderApMenu.class);
		startActivity(menuStarter);
	}

}
