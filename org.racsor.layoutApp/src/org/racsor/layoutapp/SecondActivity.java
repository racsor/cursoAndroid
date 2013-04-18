package org.racsor.layoutapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class SecondActivity extends Activity {
	private static final String CT_TAG = "SecondActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.am_second_activity);
		findViewById(R.id.am_bt_traerContactos).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				traerContactos();
			}
		});
		findViewById(R.id.am_bt_crearContacto).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				crearContactos();
			}
		});
	}

	protected void crearContactos() {
		String DisplayName = "XYZ";
		String MobileNumber = "123456";
		String HomeNumber = "1111";
		String WorkNumber = "2222";
		String emailID = "email@nomail.com";
		String company = "bad";
		String jobTitle = "abcd";

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

		// ------------------------------------------------------ Names
		if (DisplayName != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DisplayName).build());
		}

		// ------------------------------------------------------ Mobile Number
		if (MobileNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build());
		}

		// ------------------------------------------------------ Home Numbers
		if (HomeNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME).build());
		}

		// ------------------------------------------------------ Work Numbers
		if (WorkNumber != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK).build());
		}

		// ------------------------------------------------------ Email
		if (emailID != null) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
					.withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK).build());
		}

		// ------------------------------------------------------ Organization
		if (!company.equals("") && !jobTitle.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
					.withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
					.withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
					.withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK).build());
		}

		// Asking the Contact provider to create a new contact
		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		Toast.makeText(this, "Contact added", Toast.LENGTH_LONG).show();
	}

	protected void traerContactos() {
		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		startManagingCursor(cursor);

		String[] result = new String[cursor.getCount()];

		for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
			int nameidx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
			int Ididx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
			String strName = cursor.getString(nameidx);
			String strId = cursor.getString(Ididx);
			String phones = accederTelefonoPorContacto(strId);

			result[cursor.getPosition()] = strName + "(" + strId + ")";
			Log.d(CT_TAG, strName + "(" + strId + " - " + phones + ")");
			Toast.makeText(this, "Contact "+strName + "(" + strId + " - " + phones + ")" , Toast.LENGTH_LONG).show();
		}
		stopManagingCursor(cursor);
	}

	private String accederTelefonoPorContacto(String contactId) {
		String ret = null;
		Cursor cursor = getContentResolver().query(Data.CONTENT_URI, new String[] { Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL },
				Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'", new String[] { String.valueOf(contactId) }, null);
		String result = new String("");

		for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
			int phonesID = cursor.getColumnIndexOrThrow(Phone.NUMBER);
			String phone = cursor.getString(phonesID);
			result += phone + " - ";
		}

		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.am_second_activity, menu);
		return true;
	}

}
