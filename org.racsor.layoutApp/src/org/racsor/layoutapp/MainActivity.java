package org.racsor.layoutapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String CT_TAG = "AMMAIN";
	private static final int CT_BORRAR = 1;
	private static final int CT_CAMBIA_TEXTO = 2;
	EditText mValor;
	TextView mAmount;
	TextView mTextoMenu;
	EditText mTextoDialogo;
	TextView mTotal;
	RadioGroup rgPercent;
	Spinner mSPTips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mValor = (EditText) findViewById(R.id.am_et_amount);
		mAmount = (TextView) findViewById(R.id.am_tv_amount);
		mTotal = (TextView) findViewById(R.id.am_tv_total);
		rgPercent = (RadioGroup) findViewById(R.id.am_rg_percent);
		mSPTips = (Spinner) findViewById(R.id.am_sp_selec);
		mTextoMenu = (TextView) findViewById(R.id.am_tv_textMenu);

		mValor.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Log.d(CT_TAG, "onTextChanged:" + mAmount.getText());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				Log.d(CT_TAG, "beforeTextChanged:" + mAmount.getText());
			}

			@Override
			public void afterTextChanged(Editable s) {
				Log.d(CT_TAG, "afterTextChanged:" + mAmount.getText());
				calculaPercent();
			}
		});

		rgPercent.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				calculaPercent();
			}
		});

		findViewById(R.id.am_rb_10).setTag(Double.valueOf(0.10));
		findViewById(R.id.am_rb_15).setTag(Double.valueOf(0.15));
		findViewById(R.id.am_rb_18).setTag(Double.valueOf(0.18));
		findViewById(R.id.am_rb_20).setTag(Double.valueOf(0.20));

		// Carga de datos a mano
		// ArrayAdapter<String> spAdapter=new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item);
		// spAdapter.add("10%");
		// spAdapter.add("15%");
		// spAdapter.add("18%");
		// spAdapter.add("20%");
		// Carga de datos mediante recursos

		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.ap_array_strings_porcentajes));
		mSPTips.setAdapter(spAdapter);

		final int porcentajes[] = getResources().getIntArray(
				R.array.ap_array_integer_porcentajes);

		mSPTips.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				Log.d(CT_TAG, "Porcentaje aplicar:" + porcentajes[pos]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		registerForContextMenu(mValor);

	}

	protected void calculaPercent() {
		Log.d(CT_TAG, "calculaPercent:" + mValor.getText());
		Toast.makeText(this, "calculando Porcentaje", Toast.LENGTH_SHORT)
				.show();

		try {
			Double porcentaje = retornaPorcentajeSeleccionado();
			String amount = getResources().getString(R.string.tipAmount) + " "
					+ Double.parseDouble(mValor.getText().toString())
					* porcentaje;
			String total = getResources().getString(R.string.tipTotal) + " "
					+ Double.parseDouble(mValor.getText().toString())
					* (1 + porcentaje);

			mAmount.setText(amount.trim());
			mTotal.setText(total.trim());
		} catch (Exception e) {
			mAmount.setText(getResources().getString(R.string.tipAmount));
			mTotal.setText(getResources().getString(R.string.tipTotal));
		}

	}

	private Double retornaPorcentajeSeleccionado() {
		int checkedRadioButton = rgPercent.getCheckedRadioButtonId();
		Double porcentaje = (Double) findViewById(checkedRadioButton).getTag();
		return porcentaje;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ap_mn_hola:
			// ponTexto("hola");
			showDialog(CT_CAMBIA_TEXTO);
			break;
		case R.id.ap_mn_adios:
			ponTexto("adios");
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v == mValor) {
			getMenuInflater().inflate(R.menu.activity_menu, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.ap_mn_hola:
			ponTexto("hola Contextual");
			showDialog(CT_BORRAR);
			break;
		case R.id.ap_mn_adios:
			ponTexto("adios contextual");
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void ponTexto(String texto) {
		// mTextoMenu.setText(getResources().getString(idtexto));
		mTextoMenu.setText(texto);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CT_BORRAR:
//			AlertDialog.Builder adb=new AlertDialog.Builder(this);
//			adb.setTitle("Borrar");
//			adb.setMessage("¿Seguro que quieres borrar?");
//			adb.setPositiveButton("Si", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					ponTexto("Vamos a borrar");
//				}
//			});
//			adb.setNegativeButton("Cancelar", null);
//			return adb.create();
			DatePickerDialog dlg=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					Toast.makeText(MainActivity.this, "Se ha seleccionado la fecha del año "+ year, Toast.LENGTH_LONG).show();
					
				}
			}, 2013, 0, 24);
			return dlg;
			
			
		case CT_CAMBIA_TEXTO:
			AlertDialog.Builder adb2=new AlertDialog.Builder(this);
			adb2.setTitle("Cambio texto");
			adb2.setMessage("Introduczca Texto");
			adb2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ponTexto(mTextoDialogo.getText().toString());
				}
			});
			adb2.setNegativeButton("Cancelar", null);
			//Incrustar layout propio
			LayoutInflater layoutInflater=getLayoutInflater();
			View view=layoutInflater.inflate(R.layout.am_dialogo_texo, null);
			mTextoDialogo = (EditText) view.findViewById(R.id.di_text);
			adb2.setView(view);
			return adb2.create();
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case CT_CAMBIA_TEXTO:
			mTextoDialogo.setText(mTextoMenu.getText());
			break;
		}
		super.onPrepareDialog(id, dialog);
	}

}
