package com.example.labs1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MNMain extends Activity {
	int mIntentos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mn_main);
		mIntentos = 0;
		findViewById(R.id.mn_bt_cambiarTexto).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						cambiaTexto();
					}
				});
	}

	protected void cambiaTexto() {
		TextView tx = (TextView) findViewById(R.id.mn_tv_cambiartexto);
		tx.setText(""+mIntentos++);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mn_main, menu);
		return true;
	}

}
