package com.example.multithreading;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AmtMultiThread extends Activity {
	int mRegistrosProcesados = 0;
	TextView mTVMsg1;
	TextView mTVMsg2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amt_muti_thread);
		mTVMsg1 = (TextView) findViewById(R.id.amt_tv_1);
		mTVMsg2 = (TextView) findViewById(R.id.amt_tv_2);
		findViewById(R.id.amt_bt_rapido).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				funcionRapida();
			}
		});
		findViewById(R.id.amt_bt_lento).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				funcionLenta();
			}
		});
	}
	
	private void refrescaRegistros(int i) {
		mRegistrosProcesados+=1;
		mTVMsg1.setText("Registros procesados: "+mRegistrosProcesados);
	}

	protected void funcionLenta() {
		refrescaRegistros(100);
	}

	private void funcionRapida() {
		refrescaRegistros(1);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.amt_muti_thread, menu);
		return true;
	}

}
