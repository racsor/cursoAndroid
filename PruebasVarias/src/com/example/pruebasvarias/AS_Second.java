package com.example.pruebasvarias;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class AS_Second extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.as_second);
		
		findViewById(R.id.as_bt_acabar).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra(APPruebas.CT_SEGUNDA_RESULTADO, "Todo ok");
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.as__second, menu);
		return true;
	}

}
