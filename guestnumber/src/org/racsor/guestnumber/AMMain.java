package org.racsor.guestnumber;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class AMMain extends Activity {
	private static final String CT_TAG = "AMMAIN";
	private static final String CT_BUNDLE_INCOGNITA = "incognita";
	private static final String CT_BUNDLE_INTENTOS = "intentos";
	private static final String CT_BUNDLE_MAXIMO = "maximo";
	private static final int CT_RESET = 1;
	private static final int CT_EDIT_MAXIM = 2;
	EditText mValor;
	EditText mEditMaximo;
	TextView mMensaje1;
	TextView mMensaje2;
	int mIncognita;
	int mIntentos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(CT_TAG, "onCreate invocat");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.am_main);
		mValor = (EditText) findViewById(R.id.am_et_valor);
		mMensaje1 = (TextView) findViewById(R.id.am_tv_mensaje1);
		mMensaje2 = (TextView) findViewById(R.id.am_tv_mensaje2);

		findViewById(R.id.am_bt_enviar).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				computaIntento();
			}
		});
		if (savedInstanceState == null) {
			int maximo = restaurarConfiguracion();
			inicializaIncognita(maximo);
		} else {
			restaurarEstat(savedInstanceState);
		}
	}
	
	private void creaNotificacion(){
		Notification not=new Notification(R.drawable.ic_launcher,"incógnita acertada", System.currentTimeMillis());
		Intent intents=new Intent(this,AMMain.class);
		PendingIntent pIntent=PendingIntent.getActivity(this, 0, intents, Notification.FLAG_AUTO_CANCEL);
		not.setLatestEventInfo(this, "Incógnita", "Se acertó la incógnita", pIntent);
		NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.notify(1, not);
	}

	private int restaurarConfiguracion() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String max = pref.getString(getResources().getString(R.string.pref_valor_maximo), "100");
		int maximo = 0;
		try {
			maximo = Integer.valueOf(max);
		} catch (Exception e) {
			maximo = 100;
		}

		return maximo;
	}

	private void restaurarEstat(Bundle savedInstanceState) {
		Log.d(CT_TAG, "restaurarEstat invocat");
		mIncognita = savedInstanceState.getInt(CT_BUNDLE_INCOGNITA);
		mIntentos = savedInstanceState.getInt(CT_BUNDLE_INTENTOS);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(CT_TAG, "onSaveInstanceState invocat");
		super.onSaveInstanceState(outState);
		outState.putInt(CT_BUNDLE_INCOGNITA, mIncognita);
		outState.putInt(CT_BUNDLE_INTENTOS, mIntentos);
	}

	private void inicializaIncognita(int rango) {
		Log.d(CT_TAG, "inicializaIncognita invocat");
		mIntentos = 0;
		mIncognita = new Random().nextInt(rango) + 1;
		Log.d(CT_TAG, "La incongita es:" + mIncognita);
		mMensaje2.setText(getMensajeIntento(mIntentos));
	}

	private void computaIntento() {
		mIntentos++;
		int valAct;
		try {
			valAct = Integer.valueOf(mValor.getText().toString()).intValue();

		} catch (Exception e) {
			mMensaje1.setText(R.string.am_val_invalido);
			return;
		}
		if (valAct == mIncognita) {
			mMensaje1.setText(R.string.am_val_acertado);
			findViewById(R.id.am_bt_enviar).setEnabled(false);
			creaNotificacion();
		} else if (valAct < mIncognita) {
			mMensaje1.setText(getResources().getString(R.string.am_val_mayor, valAct));
		} else {
			mMensaje1.setText(getResources().getString(R.string.am_val_menor, valAct));
		}
		mMensaje2.setText(getMensajeIntento(mIntentos));
	}

	public String getMensajeIntento(int mIntentos) {
		return getResources().getQuantityString(R.plurals.am_val_intento, mIntentos, mIntentos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(CT_TAG, "method onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.am_menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.am_menu_reset:
			Log.d(CT_TAG, "method onOptionsItemSelected am_menu_reset");
			showDialog(CT_RESET);
			break;
		case R.id.am_menu_editmax:
			Log.d(CT_TAG, "method onOptionsItemSelected am_menu_editmax");
			showDialog(CT_EDIT_MAXIM);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CT_RESET:
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			Editor editor = pref.edit();
			editor.commit();
			editor.putString(getResources().getString(R.string.pref_valor_maximo), mEditMaximo.getText().toString());

			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Reiniciar");
			adb.setMessage("¿Seguro que quieres reiniciar?");
			adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					inicializaIncognita(Integer.parseInt(mEditMaximo.getText().toString()));
					mValor.setText("");
				}
			});
			adb.setNegativeButton("Cancelar", null);
			return adb.create();
		case CT_EDIT_MAXIM:
			AlertDialog.Builder adb2 = new AlertDialog.Builder(this);
			adb2.setTitle("Editar Máximo");
			adb2.setMessage("¿Seguro que quieres editar el máximo?");
			adb2.setPositiveButton("Si", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					inicializaIncognita(Integer.parseInt(mEditMaximo.getText().toString()));
				}
			});
			adb2.setNegativeButton("Cancelar", null);
			LayoutInflater layoutInflater = getLayoutInflater();
			View view = layoutInflater.inflate(R.layout.am_dialog_maximo, null);
			mEditMaximo = (EditText) view.findViewById(R.id.am_dialog_et_max);
			adb2.setView(view);
			return adb2.create();
		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onAttachedToWindow() {
		openOptionsMenu();
	};

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
	protected void onStart() {
		Log.d(CT_TAG, "method onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.d(CT_TAG, "method onStop");
		super.onStop();
	}

}
