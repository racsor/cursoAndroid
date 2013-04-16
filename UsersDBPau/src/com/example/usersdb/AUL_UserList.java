package com.example.usersdb;

import java.util.Calendar;
import java.util.Vector;

import com.example.usersdb.usuarios.BDUsuarios;
import com.example.usersdb.usuarios.Usuario;
import com.example.usersdb.usuarios.UsuarioDAO;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class AUL_UserList extends Activity {
	private static final String CT_TAG = "AUL_UserList";
	SQLiteDatabase mDB;
	UsuarioDAO mUsuarioDAO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aul_user_list);
		
		mDB = new BDUsuarios(this).getWritableDatabase();
		mUsuarioDAO = new UsuarioDAO(mDB);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aul__user_list, menu);
		return true;
	}
	
	public void hazAccion(View v) {
		//insertaUsuario();
		//actualizarUsuario4();
		//borrarUsuario(4);
		//insertaPorActivity();
		actualizaPorActivity();
	}
	
	private void insertaPorActivity() {
		startActivity(new Intent(this, AGU_GestionUsuario.class));
	}
	
	private void actualizaPorActivity() {
		Intent i = new Intent(this, AGU_GestionUsuario.class);
		i.putExtra(AGU_GestionUsuario.CT_EXTRA_ID, 2l);
		startActivity(i);
	}
	
	private void borrarUsuario(long id) {
//		Usuario u = new Usuario();
//		u.setId(id);
//		mUsuarioDAO.delete(u);
		long records = mUsuarioDAO.delete(id);
		Log.i(CT_TAG, "Usuarios borrados: " + records);
	}
	
	private void actualizarUsuario4() {
		Usuario u = new Usuario();
		u.setNombre("Rodrigo");
		u.setApellidos("Rodríguez");
		Calendar c = Calendar.getInstance();
		c.set(2005, 0, 1);
		u.setFechaNacimiento(c.getTime());
		u.setId(4);
		
		long records = mUsuarioDAO.update(u);
		Log.i(CT_TAG, "Usuarios actualizados: " + records);
	}
	
	private void insertaUsuario() {
		Usuario u = new Usuario();
		u.setNombre("Fernandito");
		u.setApellidos("Fernández");
		Calendar c = Calendar.getInstance();
		c.set(1978, 0, 1);
		u.setFechaNacimiento(c.getTime());
		
		long id = mUsuarioDAO.insertar(u);
		Log.i(CT_TAG, "Usuario insertado con id " + id);
		
	}

	public void hazQuery(View v) {
		Vector<Usuario> todos = mUsuarioDAO.getAll();
		for (Usuario u : todos) 
			Log.i(CT_TAG, u.toString());
	}
}
