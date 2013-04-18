package org.racsor.layoutapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class PracticaIntentActivity extends Activity {
	private static final String CT_TAG = "PracticaIntent";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.am_practica_intents);
		findViewById(R.id.am_bt_irgoogle).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				abrirNavegador();
			}
		});
		findViewById(R.id.am_bt_llamar).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				abrirLlamar();
			}
		});
		findViewById(R.id.am_bt_irgoogle).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				abrirNavegador();
			}
		});
		findViewById(R.id.am_bt_segunda).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				abrirSegunda();
			}
		});
	}
	protected void abrirSegunda() {
		Intent intent=new Intent("org.racsor.layoutapp.SecondActivity");
		startActivity(intent);
		
	}
	protected void abrirLlamar() {
		Intent intent=new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:856254654"));
		startActivity(intent);
	}
	protected void abrirNavegador() {
		Intent intent=new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("www.google.es"));
		startActivity(intent);
	}

}
