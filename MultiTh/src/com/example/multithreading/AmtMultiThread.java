package com.example.multithreading;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class AmtMultiThread extends Activity {
	public static final String CT_TAG = "multithreading";
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
		findViewById(R.id.amt_bt_async).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				funcionAsyncTask();
			}
		});
	}

	private void refrescaRegistros(int i) {
		mRegistrosProcesados += i;
		mTVMsg1.setText("Registros procesados: " + mRegistrosProcesados);
	}

	protected void funcionLenta() {
		Thread lentorro = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d(CT_TAG, "Soy lentorro y mi nombre Java: " + Thread.currentThread().getName());
				try {
					Thread.sleep(3 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.d(CT_TAG, "Soy lentorro y ahora estoy en : " + Thread.currentThread().getName());
						refrescaRegistros(100);
					}
				});
			}
		});
		Log.d(CT_TAG, "Voy a iniciar el thread. Soy " + Thread.currentThread().getName());
		lentorro.start();
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

	class ProcesadorBatch extends AsyncTask<Integer, Integer, Integer> {
		public static final String CT_TAG = "ProcesadorBatch:AsyncTask";

		@Override
		protected Integer doInBackground(Integer... params) {
			Log.d(CT_TAG, "doInBackground iniciado desde:" + Thread.currentThread().getName());
			try {
				for (int i = 0; i < 10; i++) {
					Thread.sleep(10 * 1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			publishProgress(100);
			Log.d(CT_TAG, "doInBackground finalizado desde:" + Thread.currentThread().getName());
			return null;
		}

		@Override
		protected void onPreExecute() {
			Log.d(CT_TAG, "onPreExecute invocado desde:" + Thread.currentThread().getName());
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.d(CT_TAG, "onPostExecute invocado desde:" + Thread.currentThread().getName());
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.d(CT_TAG, "onProgressUpdate invocado desde:" + Thread.currentThread().getName());
			for (Integer pro : values) {
				refrescaRegistros(pro);
			}
		}
	}

	protected void funcionAsyncTask() {
		ProcesadorBatch pb = new ProcesadorBatch();
		pb.execute(1);
	}

}
