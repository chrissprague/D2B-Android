package com.css.d2bAndroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * The Settings Activity, which can be reached through the options
 * menu in the Main Activity. In the Settings Activity, the user may
 * define and set various settings for the app. These settings are
 * preserved across each run of the app, using Android's
 * {@link android.content.SharedPreferences SharedPreferences}.
 *
 * @author Christopher Sprague
 * @see android.app.Activity
 */
@SuppressWarnings("WeakerAccess")
public class SettingsActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private Switch theSwitch;

    /*
    SimpleCursorAdapter mAdapter;
    private final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    private static Integer myNotificationID = 1;
    private static Notification theNotification;
    private NotificationManager mNotifyManager;
    private InputMethodManager imm;
    */

    /**
     * Called upon creation of the Settings Activity.
     * Checks against the shared preferences for the app
     * and opens the editor for the shared preferences.
     * If already set, load the proper theme that was saved
     * in the app's preferences already.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file), Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        // check sharedPreferences here for theme
        if (sharedPreferences.getBoolean(getString(R.string.SETTINGS_theme_reference), true)) {
            // light
            this.setTheme(android.R.style.Theme_Holo_Light);
        } else {
            // dark
            this.setTheme(android.R.style.Theme_Holo);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // instantiate the notification manager, used in creating/sending notifications
        // mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // save the switch component into a variable after finding it by its ID.
        theSwitch = (Switch) findViewById(R.id.switch1);

        // set the switch on/off based on the shared preferences. default is "ON" = the light color scheme.
        theSwitch.setChecked(sharedPreferences.getBoolean(getString(R.string.SETTINGS_theme_reference), true));

    }

    /**
     * Creates the options menu for the Settings Activity.
     * The Settings Activity currently doesn't feature a menu
     * of its own, so this is unused.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * What happens when the user hits the "Defaults" button.
     * Changes won't be committed until the "Save" button is hit though.
     *
     * @param view the current view
     */
    public void onDefaultsClick(@SuppressWarnings("UnusedParameters") View view) {
        new AlertDialog.Builder(this)
                .setTitle("Restore Defaults")
                .setMessage("Are you sure you want restore the default settings?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do defaults here!
                        restoreDefaults();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Define your defaults here.
     *
     * No args, no return.
     */
    private void restoreDefaults()
    {
        theSwitch.setChecked(true);
        sharedPreferencesEditor.putBoolean(getString(R.string.SETTINGS_theme_reference), true);
        this.setTheme(android.R.style.Theme_Holo_Light);
        sharedPreferencesEditor.commit();
        this.recreate();
    }

    /**
     * Called when the user presses the Save button in
     * the Settings Activity. Saves any changes made in
     * this activity into <code>sharedPreferences</code>, the app's
     * Shared Preferences. Commits the changes, and recreates
     * the activity if theme changes are to occur.
     *
     * This button will Save your changes (if there
     * are any) and then closes the Settings Activity.
     *
     * @param view the current view.
     */
    public void onSaveClick(@SuppressWarnings("UnusedParameters") View view) {
        sharedPreferencesEditor.putBoolean(getString(R.string.SETTINGS_intermediate_theme_reference), theSwitch.isChecked());
        sharedPreferencesEditor.commit();
        Toast butter = Toast.makeText(this, R.string.savedChangesToast, Toast.LENGTH_SHORT);
        butter.setGravity(Gravity.CENTER, 0, 0);
        butter.show();
        NavUtils.navigateUpFromSameTask(this);
    }

    /**
     * Called when the user presses the cancel button.
     * This button will cancel/discard changes (if
     * there are any) made and then close the Settings
     * Activity.
     *
     * @param view - the current view.
     */
    public void onCancelClick(@SuppressWarnings("UnusedParameters") View view) {
        // revert the theme setting to the intermediate value (determined when
        // the settings activity is first launched from the main activity
        sharedPreferencesEditor.putBoolean(getString(R.string.SETTINGS_theme_reference),
                sharedPreferences.getBoolean(getString(R.string.SETTINGS_intermediate_theme_reference), true));
        sharedPreferencesEditor.commit();

        // TODO ask-before discarding changes?
        NavUtils.navigateUpFromSameTask(this);
    }

    /**
     * What happens when the user hits the switch ("Color Scheme") switcher
     *
     * @param view the current view
     */
    public void onSwitchClick(@SuppressWarnings("UnusedParameters") View view) {
        sharedPreferencesEditor.putBoolean(getString(R.string.SETTINGS_theme_reference),
                theSwitch.isChecked());
        if (theSwitch.isChecked()) {
            this.setTheme(android.R.style.Theme_Holo_Light);
        } else {
            this.setTheme(android.R.style.Theme_Holo);
        }
        sharedPreferencesEditor.commit();
        this.recreate();
    }
}
