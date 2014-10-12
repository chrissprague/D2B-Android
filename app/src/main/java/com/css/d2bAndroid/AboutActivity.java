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
@SuppressWarnings("WeakerAccess")
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.preference_file), Context.MODE_PRIVATE);

        // check sharedPreferences here for theme
        if (sharedPreferences.getBoolean(getString(R.string.SETTINGS_theme_reference), true)) {
            // light
            this.setTheme(android.R.style.Theme_Holo_Light);
        } else {
            // dark
            this.setTheme(android.R.style.Theme_Holo);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * view project on the interwebs
     *
     * @param view the calling view.
     */
    public void doViewProject(@SuppressWarnings("UnusedParameters") View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/chrissprague/D2B-Android"));
        startActivity(browserIntent);
    }
}
