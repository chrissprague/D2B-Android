package com.css.d2bAndroid;

import android.app.Activity;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
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
