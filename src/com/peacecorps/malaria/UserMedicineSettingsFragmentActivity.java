package com.peacecorps.malaria;

/**
 * Created by Chimdi on 6/2/2014.
 */

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.peacecorps.malaria.R;

public class UserMedicineSettingsFragmentActivity extends FragmentActivity
		implements AdapterView.OnItemSelectedListener {

	private static Button mDoneButton;

	private static TextView timePickButton;
	private TextView mSetupLabel;
	private TextView mDrugTakeLabel;
	private TextView mTimePickLabel;
	private TextView mIfForgetLabel;
	private Spinner mDrugSelectSpinner;
	private static String mDrugPicked;
	private static int mHour;
	private static int mMinute;
	private final static Calendar mCalendar = Calendar.getInstance();

	static SharedPreferenceStore mSharedPreferenceStore;

	public static Context mFragmentContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_user_medicine_settings);
		this.setTitle(R.string.user_medicine_settings_fragment_activity_title);

		mSharedPreferenceStore = new SharedPreferenceStore();

		mFragmentContext = UserMedicineSettingsFragmentActivity.this
				.getApplicationContext();
		mDoneButton = (Button) findViewById(R.id.user_medicine_settings_activity_done_button);
		mDoneButton.setOnClickListener(mDoneButtonClickListener);

		boolean isDoneButtonChecked = false;

		timePickButton = (TextView) findViewById(R.id.user_medicine_settings_activity_time_pick_button);
		mDrugTakeLabel = (TextView) findViewById(R.id.user_medicine_settings_activity_drug_take_label);
		mSetupLabel = (TextView) findViewById(R.id.user_medicine_settings_activity_setup_label);
		mTimePickLabel = (TextView) findViewById(R.id.user_medicine_settings_activity_time_pick_label);
		mIfForgetLabel = (TextView) findViewById(R.id.user_medicine_settings_activity_if_forget_label);
		mDrugSelectSpinner = (Spinner) findViewById(R.id.user_medicine_settings_activity_drug_select_spinner);

		mSharedPreferenceStore.getSharedPreferences(this);

		setLayoutColors();

		checkInitialAppInstall();

		createDrugSelectionSpinner();

		checkIfTimeSet(isDoneButtonChecked);

		addTimePickButtonClickListener();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		if (mSharedPreferenceStore.mPrefsStore.getBoolean("com.pc.isFirstRun",
				true)) {
			mSharedPreferenceStore.mEditor.putBoolean(
					"com.pc.hasUserSetPreference", true).commit();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mSharedPreferenceStore.mPrefsStore.getBoolean("com.pc.isFirstRun",
				true)) {
			mSharedPreferenceStore.mEditor.putBoolean(
					"com.pc.hasUserSetPreference", true).commit();
		}

	}

	@Override
	protected void onPause() {

		super.onPause();
		if (mSharedPreferenceStore.mPrefsStore.getBoolean("com.pc.isFirstRun",
				true)) {
			mSharedPreferenceStore.mEditor.putBoolean(
					"com.pc.hasUserSetPreference", true).commit();
		}

	}

	private void checkInitialAppInstall() {

		if (mSharedPreferenceStore.mPrefsStore.getBoolean(
				"com.pc.hasUserSetPreference", false)) {

			mSharedPreferenceStore.mEditor.putBoolean(
					"com.pc.hasUserSetPreference", true).commit();
			mSharedPreferenceStore.mEditor
					.putBoolean("com.pc.isFirstRun", true).commit();

			startActivity(new Intent(UserMedicineSettingsFragmentActivity.this,
					MainActivity.class));
			finish();
		}

	}

	private void createDrugSelectionSpinner() {

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.user_medicine_settings_activity_drug_array,
				android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mDrugSelectSpinner.setAdapter(adapter);

		mDrugSelectSpinner.setOnItemSelectedListener(this);
	}

	private void setLayoutColors() {
		// Text Color settings for labels and button texts for the screen
		mSetupLabel.setHintTextColor(getResources().getColor(
				R.color.user_medicine_first_text_color));
		mDoneButton.setHintTextColor(getResources().getColor(
				R.color.user_medicine_first_text_color));
		mDrugTakeLabel.setHintTextColor(getResources().getColor(
				R.color.user_medicine_second_text_color));
		mTimePickLabel.setHintTextColor(getResources().getColor(
				R.color.user_medicine_second_text_color));
		mIfForgetLabel.setHintTextColor(getResources().getColor(
				R.color.user_medicine_second_text_color));

	}

	public void addTimePickButtonClickListener() {

		timePickButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getSupportFragmentManager(), "Time Picker");
			}

		});
	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
			int minute = mCalendar.get(Calendar.MINUTE);

			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {

			boolean isDoneButtonChecked = true;

			mHour = hourOfDay;
			mMinute = minutes;

			updateTime(hourOfDay, minutes);
			checkIfTimeSet(isDoneButtonChecked);

		}

	}

	public static void checkIfTimeSet(boolean isDoneButtonChecked) {
		mDoneButton.setEnabled(isDoneButtonChecked);
	}

	public static void saveUserTimeAndMedicationPrefs() {

		int checkDay = mCalendar.get(Calendar.DAY_OF_WEEK);

		mSharedPreferenceStore.mEditor.putInt("com.pc.timeHours", mHour);
		mSharedPreferenceStore.mEditor.putInt("com.pc.timeMinutes", mMinute);
		mSharedPreferenceStore.mEditor.putInt("com.pc.dayTakingDrug", checkDay);

		mSharedPreferenceStore.mEditor.putString("com.pc.drugPicked",
				mDrugPicked);
		mSharedPreferenceStore.mEditor.putBoolean(
				"com.pc.hasUserSetPreference", true);

		mSharedPreferenceStore.mEditor.putBoolean("com.pc.isDrugTaken", false)
				.commit();
		mSharedPreferenceStore.mEditor.commit();
		mFragmentContext.startService(new Intent(mFragmentContext,
				AlarmService.class));

	}

	// converts 24hr format to 12hr format with AM/PM values
	private static void updateTime(int hours, int mins) {
		String timeSet;
		if (hours > 12) {
			hours -= 12;
			timeSet = "PM";
		} else if (hours == 0) {
			hours += 12;
			timeSet = "AM";
		} else if (hours == 12)
			timeSet = "PM";
		else
			timeSet = "AM";

		String minutes;
		if (mins < 10)
			minutes = "0" + mins;
		else
			minutes = String.valueOf(mins);

		// Append the time to a stringBuilder
		String theTime = String.valueOf(hours) + ':' + minutes + " " + timeSet;

		// Set the timePickButton as the converted time
		timePickButton.setText(theTime);
		mSharedPreferenceStore.mEditor.putInt("com.pc.AlarmHour", mHour)
				.commit();
		mSharedPreferenceStore.mEditor.putInt("com.pc.AlarmMinute", mMinute)
				.commit();
	}

	// Listeners
	private View.OnClickListener mDoneButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {

			saveUserTimeAndMedicationPrefs();

			startActivity(new Intent(UserMedicineSettingsFragmentActivity.this,
					MainActivity.class));
			finish();

		}
	};

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		mDrugPicked = parent.getItemAtPosition(position).toString();

		parent.setSelection(parent.getSelectedItemPosition());

		if (position == 2) {
			mSharedPreferenceStore.mEditor.putBoolean("com.pc.isWeekly", true);
			mSharedPreferenceStore.mEditor.putLong("com.pc.weeklyDate",
					new Date().getTime()).commit();
		} else {
			mSharedPreferenceStore.mEditor.putBoolean("com.pc.isWeekly", false);
			mSharedPreferenceStore.mEditor.putLong("com.pc.timeCounter", Calendar.getInstance().getTimeInMillis());
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

}
