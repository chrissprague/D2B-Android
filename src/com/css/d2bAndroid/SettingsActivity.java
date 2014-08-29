package com.css.d2bAndroid;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;

public class SettingsActivity extends Activity {
	
	SimpleCursorAdapter mAdapter;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor spe;
	
	private Switch theSwitch;
	private final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
	private static Integer myNotificationID = 001;
	private static Notification theNotification;
	private NotificationManager mNotifyManager;
	private InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		sp  = this.getSharedPreferences(
				getString(R.string.preference_file), Context.MODE_PRIVATE);
		spe = sp.edit();
		
		// check sp here for theme
		if ( sp.getBoolean(getString(R.string.SETTINGS_theme_reference), true) )
		{
			// light
			this.setTheme(android.R.style.Theme_Holo_Light);
		}
		else
		{
			// dark
			this.setTheme(android.R.style.Theme_Holo);
		}
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settings);
		
		mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		theSwitch = (Switch)findViewById(R.id.switch1);
		theSwitch.setChecked(sp.getBoolean(getString(R.string.SETTINGS_theme_reference), true));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * What happens when the user hits the "Defaults" button.
	 * Changes won't be committed until the "Save" button is hit though.
	 * @param view - the current view
	 */
	public void onDefaultsClick(View view)
	{
		// TODO revert all settings to defaults in here.
	}
	
	public void onSaveClick(View view)
	{
		spe.commit();
		this.recreate();
	}
	
	public void onCancelClick(View view)
	{
		// TODO discard changes?
	}
	
	/**
	 * What happens when the user hits the switch ("Color Scheme") switcher
	 * @param view - the current view
	 */
	public void onSwitchClick(View view)
	{
		spe.putBoolean(getString(R.string.SETTINGS_theme_reference), theSwitch.isChecked());
		if ( theSwitch.isChecked() )
		{
			this.setTheme(android.R.style.Theme_Holo_Light);
		} else 
		{
			this.setTheme(android.R.style.Theme_Holo); 
		}
	}
}
