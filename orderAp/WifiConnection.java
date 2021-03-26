package com.application.orderAp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WifiConnection extends Activity implements View.OnClickListener {

	Button bYes, bNo;
	TextView tvMessage, backgroundText;
	String msgConnect = "Enabeling your WiFi";
	Toast connect;
	Intent in = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_connect_to_wifi);

		bYes = (Button) findViewById(R.id.bYes);
		bNo = (Button) findViewById(R.id.bNo);
		tvMessage = (TextView) findViewById(R.id.tvMessage);
		backgroundText = (TextView) findViewById(R.id.backgroundTextWifi);

		Typeface fontFifa = Typeface.createFromAsset(getAssets(),"fifawelcome.ttf");
		
		bYes.setTypeface(fontFifa);
		bNo.setTypeface(fontFifa);
		tvMessage.setTypeface(fontFifa);
		backgroundText.setTypeface(fontFifa);

		bYes.setOnClickListener(this);
		bNo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bYes:
			connect = Toast.makeText(getApplicationContext(), msgConnect,
					Toast.LENGTH_LONG);
			connect.show();
			new ConnectToWifi().execute();
			break;

		case R.id.bNo:
			finish();
			break;
		}

	}

	class ConnectToWifi extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			bYes.setClickable(false);

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			WifiManager wifiM = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			wifiM.setWifiEnabled(true);
			startActivity(in);
			SupplicantState supState;
			WifiInfo wifiInfo = wifiM.getConnectionInfo();
			supState = wifiInfo.getSupplicantState();
			int i = 0;
			boolean check = true;
			while ((i < 50) && (check)) {
				Log.d("supState.toString() ", supState.toString());
				if (supState.toString() == "COMPLETED") {
					check = false;
					i++;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent i = new Intent(getApplicationContext(), LiveOrderCategoryMenu.class);
			startActivity(i);
		}

	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();

	}

}
