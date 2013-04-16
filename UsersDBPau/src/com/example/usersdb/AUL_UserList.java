package com.example.usersdb;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.usersdb.usuarios.BDUsuarios;
import com.example.usersdb.usuarios.Usuario;
import com.example.usersdb.usuarios.UsuarioDAO;

public class AUL_UserList extends Activity {
	private static final String CT_TAG = "AUL_UserList";
	SQLiteDatabase mDB;
	UsuarioDAO mUsuarioDAO;
	ListView mLVusuarios;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aul_user_list);

		mDB = new BDUsuarios(this).getWritableDatabase();
		mUsuarioDAO = new UsuarioDAO(mDB);

		mLVusuarios = (ListView) findViewById(R.id.au_lv_usuarios);
		registerForContextMenu(mLVusuarios);

		// prepararListViewDesdeArrayAdapter();
		prepararListViewDesdeCursorAdapter();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mn_options_add:
			insertaPorActivity();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v == mLVusuarios) {
			getMenuInflater().inflate(R.menu.aul_contextual, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.mn_editar:
			actualizaPorActivity(info.id);
			break;
		case R.id.mn_borrar:
			borrarUsuario(info.id);
			SimpleCursorAdapter adp = (SimpleCursorAdapter) mLVusuarios.getAdapter();
			adp.getCursor().requery();
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void prepararListViewDesdeCursorAdapter() {
		Cursor cursor = mUsuarioDAO.getAllAsCursor();
		String from[] = { UsuarioDAO.CT_ID, UsuarioDAO.CT_APELLIDOS };
		int to[] = { R.id.au_li_tv_id, R.id.au_li_tv_nombre };
		SimpleCursorAdapter simpleAdapter = new SimpleCursorAdapter(this, R.layout.aul_list_item, cursor, from, to) {

			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				super.bindView(view, context, cursor);
				String sFecha = "";
				if (cursor.isNull(cursor.getColumnIndex(UsuarioDAO.CT_FECHA_NAC))) {
					sFecha = Usuario.getFechaNacimientoString(null);
				} else {
					sFecha = Usuario.getFechaNacimientoString(new Date(cursor.getColumnIndex(UsuarioDAO.CT_FECHA_NAC)));
				}
				((TextView) view.findViewById(R.id.au_li_tv_fnacimiento)).setText(sFecha);
				TextView tvNombre = (TextView) view.findViewById(R.id.au_li_tv_nombre);
				String texto = tvNombre.getText().toString();
				texto += " ,";
				texto += cursor.getString(cursor.getColumnIndex(UsuarioDAO.CT_NOMBRE));
				tvNombre.setText(texto);
			}

		};
		mLVusuarios.setAdapter(simpleAdapter);

		// para cerrar el cursor automaticamente
		startManagingCursor(cursor);

	}

	private void prepararListViewDesdeArrayAdapter() {
		ArrayAdapter<Usuario> usuarioArrayAdapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, mUsuarioDAO.getAll());
		mLVusuarios.setAdapter(usuarioArrayAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aul__user_list, menu);
		return true;
	}

	public void hazAccion(View v) {
		// insertaUsuario();
		// actualizarUsuario4();
		// borrarUsuario(4);
		insertaPorActivity();
		// actualizaPorActivity(2);
	}

	private void insertaPorActivity() {
		startActivity(new Intent(this, AGU_GestionUsuario.class));
	}

	private void actualizaPorActivity(long id) {
		Intent i = new Intent(this, AGU_GestionUsuario.class);
		i.putExtra(AGU_GestionUsuario.CT_EXTRA_ID, id);
		startActivity(i);
	}

	private void borrarUsuario(long id) {
		// Usuario u = new Usuario();
		// u.setId(id);
		// mUsuarioDAO.delete(u);
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
