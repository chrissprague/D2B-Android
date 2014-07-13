package com.css.d2bAndroid;

import com.css.d2bAndroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.css.d2bAndroid.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendMessage(View view) {
		Intent intent1 = new Intent(this, D2BMessageActivity.class);
		EditText editText1 = (EditText) findViewById(R.id.edit_message1);
		Intent intent2 = new Intent(this, D2BMessageActivity.class);
		EditText editText2 = (EditText) findViewById(R.id.edit_message2);
		String message1 = editText1.getText().toString();
		String message2 = editText2.getText().toString();
		
		try {
			
			// d2b
			Integer.parseInt(message1); // input has to be an integer
			if ( message1.equals( "" ) ) { // no user input
				return; // note: do not use == "" ; for comparisons... Java 101...
			} else {
				intent1.putExtra(EXTRA_MESSAGE, message1);
				startActivity(intent1);
			}
			
			// b2d
			Integer.parseInt(message2);
			if ( message2.equals("" ) ) {
				return;
			} else {
				intent2.putExtra(EXTRA_MESSAGE, message2);
				//intent2.putExtra(EXTRA_MESSAGE, "b2d");
				startActivity(intent2);
			}
			
		} catch ( Exception ex )
		{
			ex.printStackTrace();
			return;
		}
		
		return;
	}

}
