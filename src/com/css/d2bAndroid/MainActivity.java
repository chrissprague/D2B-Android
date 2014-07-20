package com.css.d2bAndroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.css.d2bAndroid.MESSAGE";
	private static B2DConversionLogic b;
	private static D2BConversionLogic d;
	private EditText edit_message1;
	private EditText edit_message2;
	private EditText conversionResults;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		edit_message1 = (EditText)findViewById(R.id.edit_message1);
		edit_message2 = (EditText)findViewById(R.id.edit_message2);
		conversionResults = (EditText)findViewById(R.id.conversionResults);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendMessage(View view) {
		String message1 = edit_message1.getText().toString();
		String message2 = edit_message2.getText().toString();
		boolean didDToB = false;
		final Button d2bButton = (Button) findViewById(R.id.d_to_b_button);
		d2bButton.setActivated(false);
		edit_message1.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable arg0) {
				d2bButton.setActivated(true);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		try {
			
			// d2b
			Integer.parseInt(message1); // input has to be an integer
			if ( ! message1.equals( "" ) ) { // no user input
				return; // note: do not use == "" ; for comparisons... Java 101...
			} else {
				d = new D2BConversionLogic(Integer.parseInt(message1));
				conversionResults.setText(d.dtob());
				edit_message1.setText("");
				didDToB = true;
			}
			
			// b2d
			Integer.parseInt(message2);
			if ( message2.equals("") || didDToB ) {
				return;
			} else {
				b = new B2DConversionLogic(Integer.parseInt(message2));
				conversionResults.setText(b.btod());
				edit_message2.setText("");
			}
			
		} catch ( Exception ex )
		{
			ex.printStackTrace();
			return;
		}
		
		return;
	}

}
