package com.example.multithreading;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
	public static final String CT_TAG = "MyReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(CT_TAG,"Actualización de servicio ("+intent.getIntExtra(ServicioRegistros.CT_BROADCAST_PENDIENTES,0)+"/ "+intent.getIntExtra(ServicioRegistros.CT_BROADCAST_PROCESADOS,0)+" )");
	}

}
