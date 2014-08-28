package com.css.d2bAndroid;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnItemClickListener {
	
	SimpleCursorAdapter mAdapter;
	
	private ArrayAdapter<String> l;
	private ListPopupWindow lpw;
	
	private final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
	private static Integer myNotificationID = 001;
	private static Notification theNotification;
	private NotificationManager mNotifyManager;
	private InputMethodManager imm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		TextView t = (TextView)findViewById(R.id.textView1);
		// Inflate the menu; this adds items to the action bar if it is present.
		l= new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, populateSettings());
		lpw = new ListPopupWindow(SettingsActivity.this);
		lpw.setAdapter(l);
		lpw.setHeight(300);
		lpw.setWidth(400);
		lpw.setModal(true);
		lpw.setOnItemClickListener(this);
		//lpw.setAnchorView((TextView)findViewById(R.id.textView1));
		t.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lpw.show();
			}
			
		});
		return true;
	}
	
	public void inflateLPW (View view )
	{
		lpw.show();
	}
	
	private static final ArrayList<String> populateSettings()
	{
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("Hello, World");
		ret.add("sklsjdflkjsd");
		return ret;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		System.out.println("hehe");
	}
}
