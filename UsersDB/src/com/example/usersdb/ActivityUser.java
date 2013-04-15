package com.example.usersdb;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.usersdb.usuarios.BDUsuarios;
import com.example.usersdb.usuarios.Usuario;
import com.example.usersdb.usuarios.UsuariosDAO;

public class ActivityUser extends Activity {
	private static final String CT_TAG = "ActivityUser";
	private static final String CT_OBJECT_USER = "User";
	SQLiteDatabase mDB;
	UsuariosDAO mUsuarioDAO;
	EditText userId;
	EditText userName;
	EditText userLastName;
	DatePicker userFechaNacimiento;
	boolean actualizar = false;
	private Usuario user;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		Log.d(CT_TAG, "onCreate invocat");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ly_user);

		userId = (EditText) findViewById(R.id.et_user_id);
		userName = (EditText) findViewById(R.id.et_user_name);
		userLastName = (EditText) findViewById(R.id.et_user_lastname);
		userFechaNacimiento = (DatePicker) findViewById(R.id.dt_fechaNacimiento);

		mDB = new BDUsuarios(this).getWritableDatabase();
		mUsuarioDAO = new UsuariosDAO(mDB);
		if (savedInstanceState != null) {
			paintUser((Usuario) savedInstanceState.getSerializable(CT_OBJECT_USER));
		} else {
			try {
				String stringId = (String) this.getIntent().getExtras().get(AUL_UserList.CT_TAG_USER_ID);
				long id = Long.parseLong(stringId);
				paintUser(getUser(id));
			} catch (Exception e) {
				Log.e(CT_TAG, e.getMessage());
				e.printStackTrace();
			}
		}

	}

	private void paintUser(Usuario user) {
		userId.setText("" + user.getmId());
		userId.setEnabled(false);
		userName.setText("" + user.getmNombre());
		userLastName.setText("" + user.getmApellidos());
		final Calendar c = Calendar.getInstance();
		Date fNacimiento = user.getmFechaNacimiento();
		c.setTime(fNacimiento);
		userFechaNacimiento.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);
	}

	private Usuario getUser(long id) {
		user = new Usuario();
		user.setmId(0);
		user.setmNombre("");
		user.setmApellidos("");
		user.setmFechaNacimiento(new Date());
		if (id > 0) {
			actualizar = true;
			user.setmId(id);
			user = mUsuarioDAO.findById(user);
		}
		return user;
	}

	@Override
	protected void onPause() {
		super.onStop();
		Log.d(CT_TAG, "invocar onStop");
		try {
			user = new Usuario();
			if (actualizar) {
				Log.d(CT_TAG, "invocar onStop:actualizar");
				user.setmId(Long.parseLong(userId.getText().toString()));
				user.setmNombre(userName.getText().toString());
				user.setmApellidos(userLastName.getText().toString());
				final Calendar c = Calendar.getInstance();
				c.set(userFechaNacimiento.getYear(), userFechaNacimiento.getMonth(), userFechaNacimiento.getDayOfMonth());
				user.setmFechaNacimiento(c.getTime());
				mUsuarioDAO.update(user);
			} else {
				user.setmNombre(userName.getText().toString());
				user.setmApellidos(userLastName.getText().toString());
				final Calendar c = Calendar.getInstance();
				c.set(userFechaNacimiento.getYear(), userFechaNacimiento.getMonth(), userFechaNacimiento.getDayOfMonth());
				user.setmFechaNacimiento(c.getTime());
				mUsuarioDAO.insertar(user);
			}

		} catch (Exception e) {
			Log.e(CT_TAG, "Error en el onStop");
			Log.e(CT_TAG, e.getMessage());
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(CT_TAG, "onSaveInstanceState invocat");
		super.onSaveInstanceState(outState);
		outState.putSerializable(CT_OBJECT_USER, user);
	}

}
