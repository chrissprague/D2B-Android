package com.css.d2bAndroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The primary activity, in which all the conversions are done,
 * and the majority of user interaction will occur.<BR><BR>
 * The About and Settings activities may be accessed from the
 * Main Activity's option menu at the top right.
 *
 * @author Christopher Sprague
 */
public class MainActivity extends Activity {

    private SharedPreferences sp;

    private static final ArrayList<String> input_array = new ArrayList<>();
    private static final ArrayList<String> output_array = new ArrayList<>();
    private final OnItemSelectedListener conversion_listener = getSpinnerOnItemSelectedListener();
    private final TextWatcher text_watcher = getEditTextTextWatcher();

    private Spinner input_spinner;
    private Spinner output_spinner;

    private EditText input_message;
    private TextView conversion_results;

    /**
     * populate the array lists for input/output which, in turn,
     * populate the adapters which, in turn, populate the
     * input and output spinners.<BR><BR>
     * This method is called when the main activity is created,
     * and successive calls are safe as the arrays will always
     * be cleared before any new items are added.
     */
    private void populateArrays() {
        // clear before putting stuff in
        input_array.removeAll(input_array);
        output_array.removeAll(output_array);

        input_array.add(getString(R.string.decimal));
        input_array.add(getString(R.string.binary));
        output_array.add(getString(R.string.binary));
        output_array.add(getString(R.string.decimal));
        // add more conversion types here
    }

    /**
     * Called when the Main activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateArrays();

        sp = this.getSharedPreferences(
                getString(R.string.preference_file), Context.MODE_PRIVATE);

        PackageInfo pkgInfo;
        int the_theme = 0;
        try {
            pkgInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            the_theme = pkgInfo.applicationInfo.theme;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
            System.exit(4);
        }
        // check sp here for theme
        if (sp.getBoolean(getString(R.string.SETTINGS_theme_reference), true)) {
            if (!(android.R.style.Theme_Holo_Light == the_theme)) {
                this.setTheme(android.R.style.Theme_Holo_Light);
                // light
            }
        } else {
            if (!(android.R.style.Theme_Holo == the_theme)) {
                this.setTheme(android.R.style.Theme_Holo);
                // dark
            }
        }
        setContentView(R.layout.activity_main);
        input_message = (EditText) findViewById(R.id.input_message);
        input_message.addTextChangedListener(text_watcher);
        conversion_results = (TextView) findViewById(R.id.conversion_results);

        input_spinner = (Spinner) findViewById(R.id.input_type_spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, input_array);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        input_spinner.setAdapter(adapter1);
        // this is listener is SET, rather than added, so each spinner will only have a maximum of one OnItemSelectedListener
        input_spinner.setOnItemSelectedListener(conversion_listener);

        output_spinner = (Spinner) findViewById(R.id.output_type_spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, output_array);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        output_spinner.setAdapter(adapter2);
        // this is listener is SET, rather than added, so each spinner will only have a maximum of one OnItemSelectedListener
        output_spinner.setOnItemSelectedListener(conversion_listener);

        // input text view must request focus so the keyboard is "targeting" the right thing
        input_message.requestFocus();
    }

    /**
     * Inflates the menu for Main activity, including Settings and About.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This won't (necessarily) be called directly in the Java code, but instead, is hooked
     * to the About button in the main activity. This launches the About activity.
     *
     * @param m - the menu item, "About".
     */
    public void doAbout(MenuItem m) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * This won't (necessarily) be called directly in the Java code, but instead, is hooked
     * to the Settings button in the main activity. This launches the Settings activity.
     *
     * @param m - the menu item, "Settings".
     */
    public void doSettings(MenuItem m) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Analyzes the user's selection for input/output with the spinners
     * and determines which case to follow in all scenarios.<br><br>
     * The lines of code that this functionality takes up can certainly be
     * cut down, and it will be an issue when we adopt new conversion
     * types (read: hex,) but for the time being, this model is fine.
     */
    private void doConversion() {
        int the_color = sp.getBoolean(getString(R.string.SETTINGS_theme_reference), true) ?
                Color.BLACK : Color.WHITE;

        // will be converted to red if an error comes up.
        // this addresses the issue in which you go erroneous input in D->B and then
        // switch the input type to binary (and possibly other combinations.)
        conversion_results.setTextColor(the_color);
        conversion_results.setTextSize(20);

        // TODO allow for strings in !English languages
        switch (input_spinner.getSelectedItem().toString()) {
            case "Binary":
                // binary -> * (conceptually we'd have to convert first to decimal before going forward)
                switch (output_spinner.getSelectedItem().toString()) {
                    case "Decimal":
                        // b->d
                        String message2 = input_message.getText().toString();
                        if (!message2.equals("")) {
                            if (message2.length() > 31) {
                                conversion_results.setTextColor(Color.RED);
                                conversion_results.setTextSize(14);
                                conversion_results.setText("Maximum number of digits supported is 31.");
                                return;
                            }
                            try {
                                conversion_results.setTextColor(the_color);
                                conversion_results.setTextSize(20);
                                // first, validate that it's only 1's and 0's
                                for (int i = 0; i < message2.length(); i++) {
                                    if (!(message2.charAt(i) == '1' || message2.charAt(i) == '0')) {
                                        conversion_results.setTextColor(Color.RED);
                                        conversion_results.setTextSize(14);
                                        conversion_results.setText("Binary number must consist of only 1's and 0's");
                                        return;
                                    }
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                return;
                            }

                            // do conversion
                            String result = B2DConversionLogic.btod(message2);

                            // show results
                            conversion_results.setText(result);
                        }
                        break;

                    case "Binary":
                        // Binary -> Binary ; just echo input text
                        String the_message = input_message.getText().toString();
                        if (!the_message.equals("")) {
                            if (the_message.length() > 31) {
                                conversion_results.setTextColor(Color.RED);
                                conversion_results.setTextSize(14);
                                conversion_results.setText("Maximum number of digits supported is 31.");
                                return;
                            }
                            try {
                                conversion_results.setTextColor(the_color);
                                conversion_results.setTextSize(20);
                                // first, validate that it's only 1's and 0's
                                for (int i = 0; i < the_message.length(); i++) {
                                    if (!(the_message.charAt(i) == '1' || the_message.charAt(i) == '0')) {
                                        conversion_results.setTextColor(Color.RED);
                                        conversion_results.setTextSize(14);
                                        conversion_results.setText("Binary number must consist of only 1's and 0's");
                                        return;
                                    }
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                return;
                            }
                        }
                        conversion_results.setText(input_message.getText().toString());
                        break;

                    default:
                        System.err.println("Unrecognized conversion type: " + output_spinner.getSelectedItem().toString());
                        System.exit(1);
                        break;
                }
                break;

            case "Decimal":
                // covers decimal conversion: no pre-conversion necessary
                switch (output_spinner.getSelectedItem().toString()) {
                    case "Binary":
                        // do decimal -> binary
                        String message1 = input_message.getText().toString();
                        Integer the_number;
                        if (!message1.equals("")) { // no user input
                            if (message1.length() > 18) {
                                conversion_results.setTextColor(Color.RED);
                                conversion_results.setTextSize(14);
                                conversion_results.setText("Maximum number of digits supported is 18.");
                                return;
                            }
                            try {
                                conversion_results.setTextColor(the_color);
                                conversion_results.setTextSize(20);
                                // first, validate that it's valid decimal 0-9, and integer
                                for (int i = 0; i < message1.length(); i++) {
                                    if (!(Character.isDigit(message1.charAt(i)))) {
                                        conversion_results.setTextColor(Color.RED);
                                        conversion_results.setTextSize(14);
                                        conversion_results.setText("Decimal number must be an Integer (digits 0-9)");
                                        return;
                                    }
                                }
                                if (message1.length() > 9) {
                                    // need long, primitive int overflows here
                                    conversion_results.setTextSize(14);
                                    Long num = Long.parseLong(message1);
                                    String result = D2BConversionLogic.dtob_long(num);
                                    conversion_results.setText(result);
                                    return;
                                } else {
                                    conversion_results.setTextSize(20);
                                    the_number = Integer.parseInt(message1); // input has to be an integer
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                return;
                            }

                            // do conversion
                            String result = D2BConversionLogic.dtob(the_number);

                            // show results
                            conversion_results.setText(result);
                        } else {
                            // clear conversion results
                            conversion_results.setText("");
                        }

                        break;

                    case "Decimal":
                        // decimal -> decimal; just echo the input
                        conversion_results.setText(input_message.getText().toString());
                        break;

                    default:
                        System.err.println("Unrecognized conversion type: " + output_spinner.getSelectedItem().toString());
                        System.exit(1);
                        break;
                }
                break;

            default:
                System.err.println("Unrecognized conversion type: " + input_spinner.getSelectedItem().toString());
                System.exit(1);
                break;
        }
    }

    /**
     * Get the OnItemSelectedListener used by both the input spinner
     * and the output spinner, which is used to enforce that a conversion
     * occurs immediately upon switching data types.
     *
     * @return the OnItemSelectedListener used by both the input and output data type spinners.
     */
    private OnItemSelectedListener getSpinnerOnItemSelectedListener() {
        return new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // do conversion
                doConversion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
            }

        };
    }

    /**
     * get the text watcher used by the EditText which
     * collects the data which the user wants to convert
     *
     * @return the TextWatcher used by the primary EditText
     */
    private TextWatcher getEditTextTextWatcher() {
        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                doConversion();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        };
    }
}
