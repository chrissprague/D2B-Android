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
	
	private Spinner input_spinner;
	private Spinner output_spinner;
	
	private EditText input_message;
	private TextView conversion_results;
	
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
				doConversion();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
		});
		conversion_results = (TextView)findViewById(R.id.conversion_results);
		mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		input_spinner = (Spinner)findViewById(R.id.input_type_spinner);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, input_array);  
	    adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    input_spinner.setAdapter(adapter1);
	    
	    output_spinner = (Spinner)findViewById(R.id.output_type_spinner);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, output_array);  
	    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    output_spinner.setAdapter(adapter2);
	    	    
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
				conversion_results.setTextColor(Color.RED);
				conversion_results.setTextSize(14);
				conversion_results.setText("Maximum number of digits supported is 9.");
				return;
			}
			try {
				conversion_results.setTextColor(Color.BLACK);
				conversion_results.setTextSize(20);
				// first, validate that it's valid decimal 0-9, and integer
				for ( int i = 0 ; i < message1.length() ; i++ )
				{
					if ( ! ( Character.isDigit(message1.charAt(i) ) ) ) {
						conversion_results.setTextColor(Color.RED);
						conversion_results.setTextSize(14);
						conversion_results.setText("Decimal number must be an Integer (digits 0-9)");
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
			conversion_results.setText(result);
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
				conversion_results.setTextColor(Color.BLACK);
				conversion_results.setTextSize(20);
				// first, validate that it's only 1's and 0's
				for ( int i = 0 ; i < message2.length() ; i++ )
				{
					if ( ! ( message2.charAt(i) == '1' || message2.charAt(i) == '0' ) ) {
						conversion_results.setTextColor(Color.RED);
						conversion_results.setTextSize(14);
						conversion_results.setText("Binary number must consist of only 1's and 0's");
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
			conversion_results.setText(result);
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
	 * Analyzes the user's selection for input/output with the spinners
	 * and determines which case to follow in all scenarios.<br><br>
	 * The lines of code that this functionality takes up can certainly be
	 * cut down, and it will be an issue when we adopt new conversion
	 * types (read: hex,) but for the time being, this model is fine.
	 */
	private void doConversion ( ) 
	{
		switch ( input_spinner.getSelectedItem().toString() )
		{
			case "Binary":
				// binary -> * (conceptually we'd have to convert first to decimal before going forward)
				switch ( output_spinner.getSelectedItem().toString() )
				{
					case "Decimal":
						// b->d
						String message2 = input_message.getText().toString();
						if ( ! message2.equals("") ) {
							if ( message2.length() > 31 ) {
								conversion_results.setTextColor(Color.RED);
								conversion_results.setTextSize(14);
								conversion_results.setText("Maximum number of digits supported is 31.");
								return;
							}
							try {
								conversion_results.setTextColor(Color.BLACK);
								conversion_results.setTextSize(20);
								// first, validate that it's only 1's and 0's
								for ( int i = 0 ; i < message2.length() ; i++ )
								{
									if ( ! ( message2.charAt(i) == '1' || message2.charAt(i) == '0' ) ) {
										conversion_results.setTextColor(Color.RED);
										conversion_results.setTextSize(14);
										conversion_results.setText("Binary number must consist of only 1's and 0's");
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
							conversion_results.setText(result);
							break;
						}
						
					case "Binary":
						// Binary -> Binary , what's wrong with you mate?
						conversion_results.setText(input_message.getText().toString());
						break;
						
					default:
						System.err.println("Unrecognized conversion type 298" + output_spinner.getSelectedItem().toString());
						//System.exit(1);
						break;
				}
				break;
				
			case "Decimal":
				// covers decimal conversion: no pre-conversion necessary
				switch ( output_spinner.getSelectedItem().toString() )
				{
					case "Binary":						
						// do decimal -> binary
						String message1 = input_message.getText().toString();
						Integer the_number = 0;
						if ( ! message1.equals( "" ) ) { // no user input
							if ( message1.length() > 9 ) {
								conversion_results.setTextColor(Color.RED);
								conversion_results.setTextSize(14);
								conversion_results.setText("Maximum number of digits supported is 9.");
								return;
							}
							try {
								conversion_results.setTextColor(Color.BLACK);
								conversion_results.setTextSize(20);
								// first, validate that it's valid decimal 0-9, and integer
								for ( int i = 0 ; i < message1.length() ; i++ )
								{
									if ( ! ( Character.isDigit(message1.charAt(i) ) ) ) {
										conversion_results.setTextColor(Color.RED);
										conversion_results.setTextSize(14);
										conversion_results.setText("Decimal number must be an Integer (digits 0-9)");
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
							conversion_results.setText(result);
						} else {
							// clear conversion results
							conversion_results.setText("");
						}
						
						break;
					
					case "Decimal":
						// decimal -> decimal... what the heck is wrong with you mate?
						conversion_results.setText(input_spinner.getSelectedItem().toString());
						break;
						
					default:
						System.err.println("Unrecognized conversion type 357"+output_spinner.getSelectedItem().toString());
						//System.exit(1);
				}
				break;
				
			default:
				System.err.println("Unrecognized conversion type 362 "+input_spinner.getSelectedItem().toString());
				System.err.println(input_spinner.getSelectedItem().toString().equals("Decimal"));
				//System.exit(1);
		}
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
