package com.css.d2bAndroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * the about activity, with about information and
 * button linking to project view
 * 
 * @author Christopher Sprague
 */
public class AboutActivity extends Activity {
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		sp  = this.getSharedPreferences(
				getString(R.string.preference_file), Context.MODE_PRIVATE);
		
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
		setContentView(R.layout.activity_about);
		setTitle("About D2B-Android"); // TODO not like this, use @ string and don't set the title like this
	}
	
	/**
	 * view project on the interwebs
	 * @param view the calling view.
	 */
	public void doViewProject(View view)
	{
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/chrissprague/D2B-Android"));
		startActivity(browserIntent);
	}
}
