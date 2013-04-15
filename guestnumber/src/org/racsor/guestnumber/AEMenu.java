package org.racsor.guestnumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class AEMenu extends Activity {
	private static final String CT_TAG = "AEMenu";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(CT_TAG, "onCreate invocat");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ae_menu);
		findViewById(R.id.ae_bt_adivinator).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d(CT_TAG, "method OnClickListener.onClick");
						invocaAdivinator();
					}
				});
		findViewById(R.id.ae_bt_configurator).setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Log.d(CT_TAG, "method OnClickListener.onClick");
						invocaConfigurador();
					}
				});
	}


	protected void invocaConfigurador() {
		Log.d(CT_TAG, "method invocaAdivinator");
		Intent i=new Intent(this,APPreferences.class);
		startActivity(i);
	}


	@Override
	protected void onDestroy() {
		Log.d(CT_TAG, "method onDestroy");
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		Log.d(CT_TAG, "method onPause");
		super.onPause();
	}


	@Override
	protected void onRestart() {
		Log.d(CT_TAG, "method onRestart");
		super.onRestart();
	}


	@Override
	protected void onResume() {
		Log.d(CT_TAG, "method onResume");
		super.onResume();
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(CT_TAG, "method onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onStart() {
		Log.d(CT_TAG, "method onStart");
		super.onStart();
	}


	@Override
	protected void onStop() {
		Log.d(CT_TAG, "method onStop");
		super.onStop();
	}


	protected void invocaAdivinator() {
		Log.d(CT_TAG, "method invocaAdivinator");
		Intent i=new Intent(this,AMMain.class);
		startActivity(i);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(CT_TAG, "method onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.am_menu_main, menu);
		return true;
	}

}
