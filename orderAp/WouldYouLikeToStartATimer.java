package com.application.orderAp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class WouldYouLikeToStartATimer extends Activity {
	
	public static final String KEY_FROMLIVEORDER = "fromLiveOrder";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(WouldYouLikeToStartATimer.this);
			
				alertDialog.setTitle("Stopwatch");
				alertDialog.setMessage("Would you like OrderAP to start running the stopwatch ?");
				alertDialog.setIcon(R.drawable.stopwatch);

				// Setting yes Button
				alertDialog.setPositiveButton("Yes, Please",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								
								Intent i = new Intent(getApplicationContext(), StopWatch.class);
								i.putExtra(KEY_FROMLIVEORDER, 1);
					            startActivity(i);
					           
					           
								
							}
						});
				// Setting No Button
				alertDialog.setNeutralButton("No, Thanks",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								finish();

							}
						});
				
				// Showing Alert Message
				alertDialog.show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	
		this.finish();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
}
