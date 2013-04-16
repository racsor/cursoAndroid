package com.example.multithreading;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AmtMultiThread extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amt_muti_thread);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.amt_muti_thread, menu);
		return true;
	}

}
