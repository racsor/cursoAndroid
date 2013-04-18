package com.example.multithreading;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ServicioRegistros extends Service {
	public static final String CT_TAG = "ServicioRegistros";
	public static final String CT_EXTRA_VALORES = "valores";
	public static final String CT_BROADCAST_ACCION = "com.example.multithreading.ServiciosRegistros";
	public static final String CT_BROADCAST_PROCESADOS = "procesados";
	public static final String CT_BROADCAST_PENDIENTES = "pendientes";
	int mRegistrosProcesados = 0;
	int mRegistrosPendientes = 0;
	ProcesadorBatch mProc = null;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(CT_TAG, "onStartCommand invocado con startId=" + startId + " desde el thread " + Thread.currentThread().getName());
		synchronized (this) {
			mRegistrosPendientes += intent.getIntExtra(CT_EXTRA_VALORES, 0);
		}
		if (mProc == null) {
			iniciarTareaAsincrona();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void iniciarTareaAsincrona() {
		mProc = new ProcesadorBatch();
		mProc.execute(0);
		Log.d(CT_TAG, "onStartCommand iniciio el ProcesadorBatch.");
	}

	@Override
	public void onDestroy() {
		Log.d(CT_TAG, "onDestroy se han procesados: " + mRegistrosProcesados);
		Log.d(CT_TAG, "onDestroy invocado desde el thread " + Thread.currentThread().getName());
		super.onDestroy();
		if (mProc != null) {
			mProc.cancel(true);
		}
	}

	public class RegistrosBinder extends Binder {
		public boolean isRunning() {
			return mProc != null;
		}

		public int getPendientes() {
			return mRegistrosPendientes;
		}

		public int getProcesados() {
			return mRegistrosProcesados;
		}
	}

	RegistrosBinder myBinder = new RegistrosBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(CT_TAG, "onBind invocado desde el thread " + Thread.currentThread().getName());
		return myBinder;
	}

	class ProcesadorBatch extends AsyncTask<Integer, Integer, Integer> {
		public static final String CT_TAG = "ServicioRegistros:ProcesadorBatch:AsyncTask";

		@Override
		protected Integer doInBackground(Integer... params) {
			while (mRegistrosPendientes > 0 && !isCancelled()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (ServicioRegistros.this) {
					mRegistrosPendientes--;
				}
				publishProgress(1);
				Log.d(CT_TAG, "Registro pendientes: " + mRegistrosPendientes + " del thread:" + Thread.currentThread().getName());
			}
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
			if (mRegistrosPendientes > 0) {
				iniciarTareaAsincrona();
			} else {
				stopSelf();
				mProc = null;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.d(CT_TAG, "onProgressUpdate invocado desde:" + Thread.currentThread().getName());
			mRegistrosProcesados++;

			Intent i = new Intent(CT_BROADCAST_ACCION);
			i.putExtra(CT_BROADCAST_PENDIENTES, mRegistrosPendientes);
			i.putExtra(CT_BROADCAST_PROCESADOS, mRegistrosProcesados);
			sendBroadcast(i);

			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			Log.d(CT_TAG, "onCancelled invocado desde:" + Thread.currentThread().getName());
			super.onCancelled();
		}
	}

}
