package com.css.d2bAndroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.NotificationManager;

public class MainActivity extends Activity {

	private InputMethodManager imm;
	public final static String EXTRA_MESSAGE = "com.css.d2bAndroid.MESSAGE";
	private static B2DConversionLogic b;
	private static D2BConversionLogic d;
	private EditText edit_message1;
	private EditText edit_message2;
	private EditText conversionResults;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
	
	public void doDecimalToBinary(View view) {
		String message1 = edit_message1.getText().toString();
		Integer the_number = 0;
			
		// d2b
		if ( ! message1.equals( "" ) ) { // no user input
			try {
				conversionResults.setTextColor(Color.BLACK);
				conversionResults.setTextSize(20);
				// first, validate that it's valid decimal 0-9, and integer
				for ( int i = 0 ; i < message1.length() ; i++ )
				{
					if ( ! ( Character.isDigit(message1.charAt(i) ) ) ) {
						conversionResults.setTextColor(Color.RED);
						conversionResults.setTextSize(14);
						conversionResults.setText("Decimal number must be an Integer (digits 0-9)");
						return;
					}
				}
				the_number = Integer.parseInt(message1); // input has to be an integer
			} catch (NumberFormatException ex )
			{
				ex.printStackTrace();
				return;
			}
			d = new D2BConversionLogic(the_number);
			conversionResults.setText(d.dtob());
			edit_message1.setText("");
			imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edit_message1.getWindowToken(), 0);
		}
		
		return;
	}
	
	public void doBinaryToDecimal(View view )
	{
		String message2=edit_message2.getText().toString();
		Integer the_number = 0;
		
		// b2d
		if ( ! message2.equals("") ) {
			try {
				conversionResults.setTextColor(Color.BLACK);
				conversionResults.setTextSize(20);
				// first, validate that it's only 1's and 0's
				for ( int i = 0 ; i < message2.length() ; i++ )
				{
					if ( ! ( message2.charAt(i) == '1' || message2.charAt(i) == '0' ) ) {
						conversionResults.setTextColor(Color.RED);
						conversionResults.setTextSize(14);
						conversionResults.setText("Binary number must consist of only 1's and 0's");
						return;
					}
				}
				the_number = Integer.parseInt(message2); // input has to be an integer
				b = new B2DConversionLogic(the_number);
				conversionResults.setText(b.btod());
				edit_message2.setText("");
				imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit_message2.getWindowToken(), 0);
			} catch (NumberFormatException ex )
			{
				ex.printStackTrace();
				return;
			}
		}
	}

}
