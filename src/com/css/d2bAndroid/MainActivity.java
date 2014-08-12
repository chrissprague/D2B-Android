package com.css.d2bAndroid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * The primary activity, in which all the conversions are done,
 * and the majority of user interaction will occur.
 * 
 * @author Christopher Sprague
 */
public class MainActivity extends Activity {

	private final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
	private static final ArrayList<String> input_array = new ArrayList<String>();
	private static final ArrayList<String> output_array = new ArrayList<String>();
	
	private static Integer myNotificationID = 001;
	private static B2DConversionLogic b;
	private static D2BConversionLogic d;
	
	private EditText input_message;
	private TextView conversionResults;
	
	private InputMethodManager imm;
	private static Notification theNotification;
	private NotificationManager mNotifyManager;
	
	private final void populateArrays ( )
	{
		input_array.add(getString(R.string.decimal));
		input_array.add(getString(R.string.binary));
		output_array.add(getString(R.string.binary));
		output_array.add(getString(R.string.decimal));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		populateArrays();
		
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_main);
		input_message = (EditText)findViewById(R.id.input_message);
		input_message.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void afterTextChanged(Editable s) {
				//System.out.println("yolo");
				//grab text and do updates/conversions here
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
		});
		conversionResults = (TextView)findViewById(R.id.conversionResults);
		mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		Spinner inputSpinner = (Spinner)findViewById(R.id.input_type_spinner);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, input_array);  
	    adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    inputSpinner.setAdapter(adapter1);
	    
	    Spinner outputSpinner = (Spinner)findViewById(R.id.output_type_spinner);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, output_array);  
	    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    outputSpinner.setAdapter(adapter2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void dynamicConversion(View view) {
		
	}
	
	/**
	 * When hitting the D->B button.
	 * @param view
	 */
	public void doDecimalToBinary(View view) {
		String message1 = input_message.getText().toString();
		Integer the_number = 0;
			
		// d2b
		if ( ! message1.equals( "" ) ) { // no user input
			if ( message1.length() > 9 ) {
				conversionResults.setTextColor(Color.RED);
				conversionResults.setTextSize(14);
				conversionResults.setText("Maximum number of digits supported is 9.");
				return;
			}
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
			
			// do conversion
			d = new D2BConversionLogic(the_number);
			String result = d.dtob();
			
			// show results
			conversionResults.setText(result);
			input_message.setText("");
			
			// hide keyboard
			imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(input_message.getWindowToken(), 0);
			
			// setup notification
			mBuilder.setSmallIcon(R.drawable.d2b_icon48);
			mBuilder.setTicker("Decimal to Binary Conversion\n" +
					"Your result is: "+result);
			mBuilder.setContentText("Result: "+result);
			mBuilder.setContentTitle("Decimal to Binary Conversion");
			mBuilder.setAutoCancel(true);
			mBuilder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0));
			theNotification = mBuilder.build();
			theNotification.vibrate = new long[]{0,200}; // set vibrate
			
			// do notification
			mNotifyManager.notify(myNotificationID, theNotification);
		}
				
		return;
	}
	
	/**
	 * when hitting the B->D button
	 * @param view
	 */
	public void doBinaryToDecimal(View view )
	{
		String message2 = input_message.getText().toString();
		
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
			} catch (NumberFormatException ex )
			{
				ex.printStackTrace();
				return;
			}
			
			// do conversion
			b = new B2DConversionLogic(message2);
			String result = b.btod();
			
			// show results
			conversionResults.setText(result);
			input_message.setText("");
			
			// clear the keyboard
			imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(input_message.getWindowToken(), 0);
			
			// setup notification			
			mBuilder.setSmallIcon(R.drawable.d2b_icon48);
			mBuilder.setTicker("Binary to Decimal Conversion\n" +
					"Your result is: "+result);
			mBuilder.setContentText("Result: "+result);
			mBuilder.setContentTitle("Binary to Decimal Conversion");
			mBuilder.setAutoCancel(true);
			mBuilder.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0));
			theNotification = mBuilder.build();
			theNotification.vibrate = new long[]{0,200}; // set vibrate
			
			// do notification
			mNotifyManager.notify(myNotificationID, theNotification);
			
			return;
		}
	}
	
	public void doAbout(MenuItem mi)
	{
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	/**
	 * data types
	 */
	enum DATA_TYPES {
		BINARY("Binary"),
		DECIMAL("Decimal"),
		;
		private String type;
		public String getType()
		{
			return this.type;
		}
		private DATA_TYPES ( String type )
		{
			this.type=type;
		}
	}

}
