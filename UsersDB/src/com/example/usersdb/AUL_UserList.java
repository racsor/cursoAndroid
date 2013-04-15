package com.example.usersdb;

import java.util.Calendar;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.drm.DrmStore.Action;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.View;

import com.example.usersdb.usuarios.BDUsuarios;
import com.example.usersdb.usuarios.Usuario;
import com.example.usersdb.usuarios.UsuariosDAO;

public class AUL_UserList extends Activity {
	
	private final static String CT_TAG = "AUL_UserList";
	public final static String CT_TAG_USER_ID = "userId";
	SQLiteDatabase mDB;
	UsuariosDAO mUsuarioDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aul_user_list);
		
		mDB = new BDUsuarios(this).getWritableDatabase();
		mUsuarioDAO = new UsuariosDAO(mDB);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aul_user_list, menu);
		return true;
	}

	public void hazAccion(View v) {
		
//		Usuario u = new Usuario();
//		u.setmNombre("Juan");
//		u.setmApellidos("Rodriguez");
//		Calendar c = Calendar.getInstance();
//		c.set(1958,2,28);
//		u.setmFechaNacimiento(c.getTime());
//		
//		long result = mUsuarioDAO.insertar(u);
//		Log.i(CT_TAG,"Usuario insertado con ID:" + result);
		Intent i=new Intent(this,ActivityUser.class);
		i.putExtra(CT_TAG_USER_ID,new String("-1"));
		startActivity(i);
		
		
	}
	protected void invocaActivityUser() {
		Log.d(CT_TAG, "method invocaActivityUser");
		Intent i=new Intent(this,ActivityUser.class);
		i.putExtra(CT_TAG_USER_ID,new String("2"));
		startActivity(i);
	}
	
	public void hazQuery(View v) {
//		Vector<Usuario> vector = mUsuarioDAO.getAll();
//		for (Usuario u : vector){
//			Log.i(CT_TAG,u.toString());
//		}
		invocaActivityUser();
	}
	
	public void hazUpdate(View v){
		
		Usuario u = new Usuario();
		u.setmId(1);
		u.setmNombre("Pepe");
		u.setmApellidos("Caco");
		Calendar c = Calendar.getInstance();
		c.set(1970,3,14);
		u.setmFechaNacimiento(c.getTime());
		
		long result = mUsuarioDAO.update(u);
		Log.i(CT_TAG,"Usuario actualizado con ID:" + result);
		
	}
	
}
