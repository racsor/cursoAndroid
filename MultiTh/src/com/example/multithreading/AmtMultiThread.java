package com.example.multithreading;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AmtMultiThread extends Activity {
	public static final String CT_TAG = "multithreading";
	int mRegistrosProcesados = 0;
	TextView mTVMsg1;
	TextView mTVMsg2;
	ProcesadorBatch mPB;

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
		findViewById(R.id.amt_bt_startService).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				empezarService();
			}
		});
		findViewById(R.id.amt_bt_stopservice).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pararService();
			}
		});
		findViewById(R.id.amt_bt_connecta).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				conectaServicio();
			}
		});
		findViewById(R.id.amt_bt_desconecta).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				desconectaServicio();
			}
		});
		findViewById(R.id.amt_bt_refresca).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refrescaServicio();
			}
		});
	}

	// CONEXION DIRECTA AL SERVICIO
	ServicioRegistros.RegistrosBinder mConexion = null;
	ServiceConnection mServicio = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mConexion = (ServicioRegistros.RegistrosBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mConexion = null;
		}
	};

	protected void refrescaServicio() {
		Log.d(CT_TAG, "refrescaServicio");
		if (mConexion != null) {
			if (mConexion.isRunning()) {
				mTVMsg2.setText("Actualización de servicio (" + mConexion.getPendientes() + "/ " + mConexion.getProcesados() + " )");
			} else {
				mTVMsg2.setText("El servicio está parado");
			}
		}
	}

	protected void desconectaServicio() {
		Log.d(CT_TAG, "desconectaServicio");
		mTVMsg2.setText("El servicio está desconectado");
		if (mConexion!=null){
			unbindService(mServicio);
		}
		mConexion = null;
	}

	protected void conectaServicio() {
		Log.d(CT_TAG, "conectaServicio");
		mTVMsg2.setText("El servicio está conectado");
		bindService(new Intent(this, ServicioRegistros.class), mServicio, BIND_AUTO_CREATE);
	}

	// FIN CONEXION DIRECTA AL SERVICIO
	// EJECUCIÓN DEL SERVICIO
	protected void pararService() {
		stopService(new Intent(this, ServicioRegistros.class));
	}

	protected void empezarService() {
		Intent intent = new Intent(this, ServicioRegistros.class);
		intent.putExtra(ServicioRegistros.CT_EXTRA_VALORES, 10);
		startService(intent);
	}

	private void refrescaRegistros(int i) {
		mRegistrosProcesados += i;
		mTVMsg1.setText("Registros procesados: " + mRegistrosProcesados);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mTVMsg2.setText("Actualización de servicio (" + intent.getIntExtra(ServicioRegistros.CT_BROADCAST_PENDIENTES, 0) + "/ "
					+ intent.getIntExtra(ServicioRegistros.CT_BROADCAST_PROCESADOS, 0) + " )");
		}

	};

	// me voy a suscribir
	@Override
	protected void onStart() {
		super.onStart();
		IntentFilter inf = new IntentFilter();
		inf.addAction(ServicioRegistros.CT_BROADCAST_ACCION);
		registerReceiver(mReceiver, inf);

	};

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(mReceiver);
	}

	// FIN SERVICIO

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

	class ProcesadorBatch extends AsyncTask<Integer, Integer, String> {
		public static final String CT_TAG = "ProcesadorBatch:AsyncTask";

		@Override
		protected String doInBackground(Integer... params) {
			Log.d(CT_TAG, "doInBackground iniciado desde:" + Thread.currentThread().getName());
			int ciclos = params[0];
			int registroCiclo = params[1];
			Log.d(CT_TAG, "doInBackground ciclos:" + ciclos);
			for (int i = 0; i < ciclos && !isCancelled(); i++) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				publishProgress(registroCiclo);
			}
			mPB = null;
			Log.d(CT_TAG, "doInBackground finalizado desde:" + Thread.currentThread().getName());
			return isCancelled() ? "Cancelado" : "Finalizado";
		}

		@Override
		protected void onPreExecute() {
			Log.d(CT_TAG, "onPreExecute invocado desde:" + Thread.currentThread().getName());
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
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

		@Override
		protected void onCancelled() {
			Log.d(CT_TAG, "onCancelled invocado desde:" + Thread.currentThread().getName());
			super.onCancelled();
		}
	}

	protected void funcionAsyncTask() {
		if (mPB == null) {
			mPB = new ProcesadorBatch();
			mPB.execute(/* numeroclilclos */10,/* muneroregistros */100);
		}
		Toast.makeText(this, "La tarea ya está en marcha", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPB != null) {
			mPB.cancel(true);
		}
	}

}
