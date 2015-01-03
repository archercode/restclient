package com.AutomatedRelayControl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import DateTimePicker.DateTimePicker;
import DateTimePicker.DateTimePicker.DateWatcher;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainRESTClient extends Activity implements DateWatcher,
		OnClickListener {

	private PendingIntent pendingIntent;
	private EditText edit_text;
	private AlarmManager alarmManager;
	private Intent intent;
	private String result_string;
	private SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private Date date = null;

	private NetworkConnection networkConnection;
	private NetworkInfo networkInfo;

	private RadioGroup radioGroup;
	private RadioButton radButton;
	private ConnectivityManager connMgr;

	private WriteFile writefile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

		Button btnON = (Button) findViewById(R.id.btnON);
		Button btnOFF = (Button) findViewById(R.id.btnOFF);
		Button btnTimer = (Button) findViewById(R.id.btnTimer);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);

		RadioButton setON = (RadioButton) findViewById(R.id.setOn);
		RadioButton setOFF = (RadioButton) findViewById(R.id.setOff);
		radioGroup = (RadioGroup) findViewById(R.id.radGroup);

		edit_text = (EditText) findViewById(R.id.edittext1);
		btnON.setOnClickListener(this);
		btnOFF.setOnClickListener(this);
		btnTimer.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		networkConnection = new NetworkConnection(connMgr, networkInfo);

		
		// }

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnON:
			new HttpAsyncTask()
					.execute(KeyString.setRelayOn);
			break;

		case R.id.btnOFF:
			new HttpAsyncTask()
					.execute(KeyString.setRelayOff);
			break;

		case R.id.btnCancel:
			alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
			// Tell the user about what we did.
			Toast.makeText(MainRESTClient.this, "Cancel!", Toast.LENGTH_LONG)
					.show();
			break;

		case R.id.btnTimer:

			int selectedId = radioGroup.getCheckedRadioButtonId();
			View radioButton = radioGroup.findViewById(selectedId);
			int idx = radioGroup.indexOfChild(radioButton);
			radButton = (RadioButton) findViewById(selectedId);

			Toast.makeText(this, "Turning: " + radButton.getText(),
					Toast.LENGTH_SHORT).show();
			switch (idx) {
			case 0:
				intent = new Intent(MainRESTClient.this, TurnOnService.class);
				break;
			case 1:
				intent = new Intent(MainRESTClient.this, TurnOffService.class);
				break;
			}

			intent.setFlags(PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent pendingIntent = PendingIntent.getService(this, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();

			try {
				date = form.parse(result_string);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

			calendar.setTime(date);
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pendingIntent);
			Toast.makeText(MainRESTClient.this, "Start Alarm",
					Toast.LENGTH_LONG).show();
			break;

		}
	}

	public void button_click(View view) {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);
		mDateTimePicker.setDateChangedListener(this);

		// Update demo TextViews when the "OK" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mDateTimePicker.clearFocus();
						// TODO Auto-generated method stub
						result_string = mDateTimePicker.getYear() + "-"
								+ String.valueOf(mDateTimePicker.getMonth())
								+ "-"
								+ String.valueOf(mDateTimePicker.getDay())
								+ "  "
								+ String.valueOf(mDateTimePicker.getHour())
								+ ":"
								+ String.valueOf(mDateTimePicker.getMinute());
						// if(mDateTimePicker.getHour() > 12) result_string =
						// result_string + "PM";
						// else result_string = result_string + "AM";
						edit_text.setText(result_string);
						mDateTimeDialog.dismiss();
					}
				});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimeDialog.cancel();
					}
				});

		// Reset Date and Time pickers when the "Reset" button is clicked

		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimePicker.reset();
					}
				});

		// Setup TimePicker
		// No title on the dialog window
		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();
	}

	public void onDateChanged(Calendar c) { // goi khi co thay doi tu calendar
		Log.e("",
				"" + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
						+ " " + c.get(Calendar.YEAR));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}