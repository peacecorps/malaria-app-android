package com.peacecorps.malaria;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.peacecorps.malaria.R;

public class HomeScreenFragment extends Fragment {

	static final private int INIT_HOUR = 5;
	static final private int INIT_MINUTE = 30;

	private Button mAcceptMedicationButton;
	private Button mRejectMedicationButton;
	private Button mSettingsButton;
	private TextView mCurrentDateLabel;
	private TextView mCurrentDayOfweekLabel;
	private static CharSequence mGetCurrentDate;
	private static int mDrugAcceptedCount;
	private static int drugRejectedCount;
	private Calendar mCalendar;
	private String[] mPossibledays = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };
	private static View rootView;

	int checkDay = -1;
	long getDrugTakenDate;

	static SharedPreferenceStore mSharedPreferenceStore;

	public static HomeScreenFragment newInstance() {
		return new HomeScreenFragment();
	}

	@Override
	public void onResume() {

		super.onResume();
		updateUI();

	}

	public void getSharedPreferences() {

		mSharedPreferenceStore.mPrefsStore = getActivity()
				.getSharedPreferences("com.pc.storeTimePicked",
						Context.MODE_PRIVATE);
		mSharedPreferenceStore.mEditor = mSharedPreferenceStore.mPrefsStore
				.edit();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home_screen, null);
		updateUI();
		return rootView;

	}

	public void addButtonListeners() {
		mSettingsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				mSharedPreferenceStore.mEditor.putBoolean(
						"com.pc.hasUserSetPreference", false).commit();
				startActivity(new Intent(getActivity(),
						UserMedicineSettingsFragmentActivity.class));
				getActivity().finish();

			}
		});

		mAcceptMedicationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer.create(getActivity(), R.raw.accept_button_sound)
						.start();
				mDrugAcceptedCount += 1;
				if (mSharedPreferenceStore.mPrefsStore.getBoolean(
						"com.pc.isWeekly", false)) {
					decideDrugTakenUIBoolean(true);
				} else {
					decideDrugTakenUIBoolean(false);
				}

			}
		});

		mRejectMedicationButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MediaPlayer.create(getActivity(), R.raw.reject_button_sound)
						.start();
				drugRejectedCount += 1;
				if (mSharedPreferenceStore.mPrefsStore.getBoolean(
						"com.pc.isWeekly", false)) {
					decideDrugTakenUIBoolean(true);
				} else {
					decideDrugTakenUIBoolean(false);
				}

			}
		});
	}

	public void createView() {

		mAcceptMedicationButton = (Button) rootView
				.findViewById(R.id.fragment_home_screen_accept_medication_button);
		mRejectMedicationButton = (Button) rootView
				.findViewById(R.id.fragment_home_screen__reject_medication_button);
		mSettingsButton = (Button) rootView
				.findViewById(R.id.fragment_home_screen_settings_button);
		mCurrentDateLabel = (TextView) rootView
				.findViewById(R.id.fragment_home_screen_current_date);
		mCurrentDayOfweekLabel = (TextView) rootView
				.findViewById(R.id.fragment_home_screen_current_day_of_week);

		mCurrentDateLabel.setTextColor(Color.rgb(89, 43, 21));
		mCurrentDayOfweekLabel.setTextColor(Color.rgb(89, 43, 21));
		mCurrentDateLabel.setText(mGetCurrentDate);
		mCurrentDayOfweekLabel
				.setText(decideDayofWeek(checkDay, mPossibledays));
	}

	public void updateUI() {
		getSharedPreferences();
		String drugPickedDisplay = mSharedPreferenceStore.mPrefsStore
				.getString("com.pc.drugPicked", null);
		mCalendar = Calendar.getInstance();

		checkDay = mCalendar.get(Calendar.DAY_OF_WEEK);

		if (drugPickedDisplay != null) {
			if (drugPickedDisplay.equalsIgnoreCase("Malaria (Daily")
					|| drugPickedDisplay
							.equalsIgnoreCase("Doxycycline (daily)")) {
				// user taking drug daily
			} else {
				// user is taking weekly

			}

		}
		getSettings();
		createView();
		addButtonListeners();
		getSettings();
		decideisDrugTakenUI();
	}

	public void saveUsersettings(Boolean state, Boolean isWeekly) {
		if (isWeekly) {
			mSharedPreferenceStore.mEditor.putLong("com.pc.weeklyDate",
					new Date().getTime()).commit();
			mSharedPreferenceStore.mEditor.putBoolean(
					"com.pc.isWeeklyDrugTaken", state).commit();
		} else {
			mSharedPreferenceStore.mEditor.putLong("com.pc.dateDrugTaken",
					new Date().getTime()).commit();
			mSharedPreferenceStore.mEditor.putBoolean("com.pc.isDrugTaken",
					state).commit();
		}
		mSharedPreferenceStore.mEditor.putInt("com.pc.drugRejectedCount",
				drugRejectedCount).commit();
		mSharedPreferenceStore.mEditor.putInt("com.pc.drugAcceptedCount",
				mDrugAcceptedCount).commit();

	}

	public void getSettings() {
		checkDay = mCalendar.get(Calendar.DAY_OF_WEEK);
		mGetCurrentDate = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault()).format(mCalendar.getTime());
		mDrugAcceptedCount = mSharedPreferenceStore.mPrefsStore.getInt(
				"com.pc.drugAcceptedCount", 0);
		drugRejectedCount = mSharedPreferenceStore.mPrefsStore.getInt(
				"com.pc.drugRejectedCount", 0);
		decideDayofWeek(checkDay, mPossibledays);
	}

	public void missedWeekUI() {
		mCurrentDateLabel.setTextColor(Color.RED);
		mCurrentDayOfweekLabel.setTextColor(Color.RED);
	}

	public void decideisDrugTakenUI() {
		if (mSharedPreferenceStore.mPrefsStore.getBoolean("com.pc.isWeekly",
				false)) {
			if (checkDrugTakenTimeInterval("weeklyDate") == 0) {
				if ((mSharedPreferenceStore.mPrefsStore.getBoolean(
						"com.pc.isWeeklyDrugTaken", false))) {
					isDrugTakenUI();
				} else {
					newDayUI();
				}
			} else {
				if (checkDrugTakenTimeInterval("weeklyDate") < 7
						&& checkDrugTakenTimeInterval("weeklyDate") > 0) {
					if ((mSharedPreferenceStore.mPrefsStore.getBoolean(
							"com.pc.isWeeklyDrugTaken", false))) {
						isDrugTakenUI();
					} else {
						missedWeekUI();
						newDayUI();
					}
				} else if (checkDrugTakenTimeInterval("weeklyDate") > 7) {
					missedWeekUI();
					newDayUI();
				}
			}
		} else {
			if (checkDrugTakenTimeInterval("dateDrugTaken") == 0) {
				if (mSharedPreferenceStore.mPrefsStore.getBoolean(
						"com.pc.isDrugTaken", false)) {
					isDrugTakenUI();
				} else {
					isDrugNotTakenUI();
				}

			} else {
				newDayUI();
			}
		}
	}

	public long checkDrugTakenTimeInterval(String time) {
		long interval = 0;
		long today = new Date().getTime();
		long takenDate = mSharedPreferenceStore.mPrefsStore.getLong("com.pc."
				+ time, 0);
		long oneDay = 1000 * 60 * 60 * 24;
		interval = (today - takenDate) / oneDay;
		return interval;
	}

	public void newDayUI() {
		mAcceptMedicationButton
				.setBackgroundResource(R.drawable.accept_medi_checked_);
		mRejectMedicationButton
				.setBackgroundResource(R.drawable.reject_medi_checked);
		setButtonState(true);
	}

	public void isDrugNotTakenUI() {
		mAcceptMedicationButton
				.setBackgroundResource(R.drawable.accept_medi_grayscale);
		mRejectMedicationButton
				.setBackgroundResource(R.drawable.reject_medi_checked);
		setButtonState(false);
	}

	public void isDrugTakenUI() {
		mCurrentDateLabel.setTextColor(Color.rgb(89, 43, 21));
		mCurrentDayOfweekLabel.setTextColor(Color.rgb(89, 43, 21));
		mAcceptMedicationButton
				.setBackgroundResource(R.drawable.accept_medi_checked_);
		mRejectMedicationButton
				.setBackgroundResource(R.drawable.reject_medi_grayscale);
		setButtonState(false);
	}

	public void setButtonState(boolean state) {
		mAcceptMedicationButton.setEnabled(state);
		mRejectMedicationButton.setEnabled(state);
	}

	public void decideDrugTakenUIBoolean(Boolean isWeekly) {
		if (isWeekly) {
			mSharedPreferenceStore.mEditor.putBoolean(
					"com.pc.isWeeklyDrugTaken", true).commit();
			if (checkDrugTakenTimeInterval("weeklyDate") == 0) {
				isDrugTakenUI();
			} else if (checkDrugTakenTimeInterval("weeklyDate") > 1) {
				isDrugTakenUI();
				changeWeeklyAlarmTime();
			}
		} else {
			saveUsersettings(true, isWeekly);
			isDrugTakenUI();
		}
	}

	public void changeWeeklyAlarmTime() {
		int hour = Calendar.getInstance().getTime().getHours();
		int minute = Calendar.getInstance().getTime().getMinutes() - 1;
		getActivity().startService(
				new Intent(getActivity(), AlarmService.class));
		mSharedPreferenceStore.mEditor.putInt("com.pc.AlarmHour", hour)
				.commit();
		mSharedPreferenceStore.mEditor.putInt("com.pc.AlarmMinute", minute)
				.commit();
	}

	public void decideDrugNotTakenUIBoolean(Boolean isWeekly) {
		saveUsersettings(false, isWeekly);
		isDrugNotTakenUI();
	}

	public String decideDayofWeek(int checkDay, String possibledays[]) {
		String currentDayOfWeek = null;
		switch (checkDay) {
		case 1:
			currentDayOfWeek = possibledays[0];
			break;
		case 2:
			currentDayOfWeek = possibledays[1];
			break;
		case 3:
			currentDayOfWeek = possibledays[2];
			break;
		case 4:
			currentDayOfWeek = possibledays[3];
			break;
		case 5:
			currentDayOfWeek = possibledays[4];
			break;
		case 6:
			currentDayOfWeek = possibledays[5];
			break;
		case 7:
			currentDayOfWeek = possibledays[6];
			break;

		}
		return currentDayOfWeek;
	}

	public void show(String text) {
		Toast.makeText(getActivity().getApplicationContext(), text,
				Toast.LENGTH_LONG).show();
	}

}