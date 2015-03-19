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
@SuppressWarnings("WeakerAccess")
public class MainActivity extends Activity {

    private SharedPreferences sharedPreferences;

    private static final ArrayList<String> INPUT_ARRAY = new ArrayList<>();
    private static final ArrayList<String> OUTPUT_ARRAY = new ArrayList<>();

    private final OnItemSelectedListener conversionListener = getSpinnerOnItemSelectedListener();
    private final TextWatcher textWatcher = getEditTextTextWatcher();

    private Spinner inputSpinner;
    private Spinner outputSpinner;

    private EditText inputMessage;
    private TextView conversionResults;

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
        INPUT_ARRAY.removeAll(INPUT_ARRAY);
        OUTPUT_ARRAY.removeAll(OUTPUT_ARRAY);

        INPUT_ARRAY.add(getString(R.string.decimal));
        INPUT_ARRAY.add(getString(R.string.binary));
        OUTPUT_ARRAY.add(getString(R.string.binary));
        OUTPUT_ARRAY.add(getString(R.string.decimal));
        // add more conversion types here
    }

    /**
     * Called when the Main activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateArrays();

        sharedPreferences = this.getSharedPreferences(
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

        // check sharedPreferences here for theme
        if (sharedPreferences.getBoolean(getString(R.string.SETTINGS_theme_reference), false)) {
            if (!(android.R.style.Theme_Holo_Light == the_theme)) {
                this.setTheme(android.R.style.Theme_Holo_Light);
            }
        } else {
            if (!(android.R.style.Theme_Holo == the_theme)) {
                this.setTheme( android.R.style.Theme_Holo );
            }
        }
        setContentView(R.layout.activity_main);
        inputMessage = (EditText) findViewById(R.id.input_message);
        inputMessage.addTextChangedListener(textWatcher);
        conversionResults = (TextView) findViewById(R.id.conversion_results);

        inputSpinner = (Spinner) findViewById(R.id.input_type_spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, INPUT_ARRAY);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        inputSpinner.setAdapter(adapter1);
        // this is listener is SET, rather than added, so each spinner will only have a maximum of one OnItemSelectedListener
        inputSpinner.setOnItemSelectedListener(conversionListener);

        outputSpinner = (Spinner) findViewById(R.id.output_type_spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, OUTPUT_ARRAY);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        outputSpinner.setAdapter(adapter2);
        // this is listener is SET, rather than added, so each spinner will only have a maximum of one OnItemSelectedListener
        outputSpinner.setOnItemSelectedListener(conversionListener);

        // input text view must request focus so the keyboard is "targeting" the right thing
        inputMessage.requestFocus();
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
    public void doAbout(@SuppressWarnings("UnusedParameters") MenuItem m) {
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
        // save the intermediate theme value before saving/cancelling in the settings activity.
        // this is done each time the settings activity is started via the MainActivity.
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(
                getString(R.string.SETTINGS_intermediate_theme_reference),
                sharedPreferences.getBoolean(
                        getString(R.string.SETTINGS_theme_reference), true));
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
        int the_color = sharedPreferences.getBoolean(getString(R.string.SETTINGS_theme_reference), true) ?
                Color.BLACK : Color.WHITE;

        // will be converted to red if an error comes up.
        // this addresses the issue in which you go erroneous input in D->B and then
        // switch the input type to binary (and possibly other combinations.)
        conversionResults.setTextColor(the_color);
        conversionResults.setTextSize(20);

        // TODO allow for strings in !English languages
        switch (inputSpinner.getSelectedItem().toString()) {
            case "Binary":
                // binary -> * (conceptually we'd have to convert first to decimal before going forward)
                switch (outputSpinner.getSelectedItem().toString()) {
                    case "Decimal":
                        // b->d
                        String message2 = inputMessage.getText().toString();
                        if (!message2.equals("")) {
                            if (message2.length() > 31) {
                                conversionResults.setTextColor(Color.RED);
                                conversionResults.setTextSize(14);
                                conversionResults.setText("Maximum number of digits supported is 31.");
                                return;
                            }
                            try {
                                conversionResults.setTextColor(the_color);
                                conversionResults.setTextSize(20);
                                // first, validate that it's only 1's and 0's
                                for (int i = 0; i < message2.length(); i++) {
                                    if (!(message2.charAt(i) == '1' || message2.charAt(i) == '0')) {
                                        conversionResults.setTextColor(Color.RED);
                                        conversionResults.setTextSize(14);
                                        conversionResults.setText("Binary number must consist of only 1's and 0's");
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
                            conversionResults.setText(result);
                        }
                        break;

                    case "Binary":
                        // Binary -> Binary ; just echo input text
                        String the_message = inputMessage.getText().toString();
                        if (!the_message.equals("")) {
                            if (the_message.length() > 31) {
                                conversionResults.setTextColor(Color.RED);
                                conversionResults.setTextSize(14);
                                conversionResults.setText("Maximum number of digits supported is 31.");
                                return;
                            }
                            try {
                                conversionResults.setTextColor(the_color);
                                conversionResults.setTextSize(20);
                                // first, validate that it's only 1's and 0's
                                for (int i = 0; i < the_message.length(); i++) {
                                    if (!(the_message.charAt(i) == '1' || the_message.charAt(i) == '0')) {
                                        conversionResults.setTextColor(Color.RED);
                                        conversionResults.setTextSize(14);
                                        conversionResults.setText("Binary number must consist of only 1's and 0's");
                                        return;
                                    }
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                return;
                            }
                        }
                        String echo = inputMessage.getText().toString().replaceFirst("^0+(?!$)", "");
                        conversionResults.setText(echo);
                        break;

                    default:
                        System.err.println("Unrecognized conversion type: " + outputSpinner.getSelectedItem().toString());
                        System.exit(1);
                        break;
                }
                break;

            case "Decimal":
                // covers decimal conversion: no pre-conversion necessary
                switch (outputSpinner.getSelectedItem().toString()) {
                    case "Binary":
                        // do decimal -> binary
                        String message1 = inputMessage.getText().toString();
                        Integer the_number;
                        if (!message1.equals("")) { // no user input
                            if (message1.length() > 18) {
                                conversionResults.setTextColor(Color.RED);
                                conversionResults.setTextSize(14);
                                conversionResults.setText("Maximum number of digits supported is 18.");
                                return;
                            }
                            try {
                                conversionResults.setTextColor(the_color);
                                conversionResults.setTextSize(20);
                                // first, validate that it's valid decimal 0-9, and integer
                                for (int i = 0; i < message1.length(); i++) {
                                    if (!(Character.isDigit(message1.charAt(i)))) {
                                        conversionResults.setTextColor(Color.RED);
                                        conversionResults.setTextSize(14);
                                        conversionResults.setText("Decimal number must be an Integer (digits 0-9)");
                                        return;
                                    }
                                }
                                if (message1.length() > 9) {
                                    // need long, primitive int overflows here
                                    conversionResults.setTextSize(14);
                                    Long num = Long.parseLong(message1);
                                    String result = D2BConversionLogic.dtob_long(num);
                                    conversionResults.setText(result);
                                    return;
                                } else {
                                    conversionResults.setTextSize(20);
                                    the_number = Integer.parseInt(message1); // input has to be an integer
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                                return;
                            }

                            // do conversion
                            String result = D2BConversionLogic.dtob(the_number);

                            // show results
                            conversionResults.setText(result);
                        } else {
                            // clear conversion results
                            conversionResults.setText("");
                        }

                        break;

                    case "Decimal":
                        // decimal -> decimal; just echo the input
                        String message2 = inputMessage.getText().toString();
                        if ( message2.length() == 0 )
                        {
                            return;
                        }
                        if (message2.length() > 31) {
                            conversionResults.setTextColor(Color.RED);
                            conversionResults.setTextSize(14);
                            conversionResults.setText("Maximum number of digits supported is 31.");
                            return;
                        }
                        message2.replaceFirst("^0+(?!$)", "");
                        conversionResults.setText(message2);
                        break;

                    default:
                        System.err.println("Unrecognized conversion type: " + outputSpinner.getSelectedItem().toString());
                        System.exit(1);
                        break;
                }
                break;

            default:
                System.err.println("Unrecognized conversion type: " + inputSpinner.getSelectedItem().toString());
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
