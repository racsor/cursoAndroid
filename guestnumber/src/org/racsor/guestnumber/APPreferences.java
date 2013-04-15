package org.racsor.guestnumber;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class APPreferences extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_settings);
	}
}
