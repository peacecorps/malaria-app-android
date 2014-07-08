package com.example.viewpagertest;

import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

import com.example.viewpagertest.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class MainActivity extends FragmentActivity {

	FragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAdapter = new FragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.vPager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.vIndicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if (position == 1) {
					if (FirstAnalyticFragment.checkMediLastTakenTime != null) {
						FirstAnalyticFragment.checkMediLastTakenTime
								.setText(SharedPreferenceStore.mPrefsStore
										.getString(
												"com.pc.checkMediLastTakenTime",
												"").toString());
						FirstAnalyticFragment.doses.setText(""
								+ SharedPreferenceStore.mPrefsStore.getInt(
										"com.pc.AcceptedCount", 0));

						long interval = checkDrugTakenTimeInterval("timeCounter");
						int takenCount = SharedPreferenceStore.mPrefsStore.getInt("com.pc.drugAcceptedCount", 0);
						double percentage = (takenCount/interval)* 100;
						FirstAnalyticFragment.adherence.setText(""+percentage);
					}

				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public long checkDrugTakenTimeInterval(String time) {
		long interval = 0;
		long today = new Date().getTime();
		long takenDate = SharedPreferenceStore.mPrefsStore.getLong("com.pc."
				+ time, 0);
		long oneDay = 1000 * 60 * 60 * 24;
		interval = (today - takenDate) / oneDay;
		return interval+1;
	}

}