package com.peacecorps.malaria;


import java.util.Date;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

public class MainActivity extends FragmentActivity {


    FragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    Button mInfoButton;
    Button mTripButton;
    String TAGMA="MainActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatabaseSQLiteHelper sqLite = new DatabaseSQLiteHelper(this);
        /*Method opens the Info Hub
        *Tiny 'i' symbol in the Setup Screen is Info Hub Button
        */
        mInfoButton = (Button) findViewById(R.id.infoButton);
        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication().getApplicationContext(), InfoHubFragmentActivity.class));
                finish();

            }
        });
        Log.d(TAGMA, "Info Hub Button initialized");


        mTripButton = (Button) findViewById(R.id.tripButton);
        mTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication().getApplicationContext(),TripIndicatorFragmentActivity.class));
                finish();
            }
        });

        mAdapter = new FragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.vPager);
        mPager.setAdapter(mAdapter);
        Log.d(TAGMA, "Adapter Set");
        mIndicator = (CirclePageIndicator) findViewById(R.id.vIndicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {



            @Override
            public void onPageSelected(int position) {

                if (position == 1) {


                        Log.d(TAGMA, "Inside Page Selected");
                        long interval = checkDrugTakenTimeInterval("firstRunTime");
                        int takenCount = SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.drugAcceptedCount", 0);
                        double adherenceRate;
                        Log.d(TAGMA,""+ interval);
                        Log.d(TAGMA,""+ takenCount);
                        if(interval!=0)
                        adherenceRate = ((double)takenCount / (double)interval) * 100;
                        else
                        adherenceRate = 100;
                        String ar=String.format("%.4f ",adherenceRate);
                        FirstAnalyticFragment.adherence.setText("" + ar + "%");
                    int currentDose = 0,dosesInaRow=0;
                    if (SharedPreferenceStore.mPrefsStore.getBoolean(
                            "com.peacecorps.malaria.isWeekly", false)) {
                        currentDose = SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.weeklyDose", 0);
                    } else {
                        //currentDose = SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.dailyDose", 0);
                        dosesInaRow=sqLite.getDosesInaRow();
                        SharedPreferenceStore.mEditor.putInt("com.peacecorps.malaria.dailyDose",dosesInaRow).apply();
                        currentDose=dosesInaRow;
                    }
                    FirstAnalyticFragment.doses.setText("" + currentDose);
                    Log.d(TAGMA, "Doses in a Row:" + dosesInaRow);




                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }


        });

    }

    public long checkDrugTakenTimeInterval(String time) {
        long interval = 0;
        long today = new Date().getTime();
        Log.d(TAGMA,"today:"+ today);
        long takenDate = SharedPreferenceStore.mPrefsStore.getLong("com.peacecorps.malaria."
                + time, 0);
        Log.d(TAGMA,"taken date:"+ takenDate);
        long oneDay = 1000 * 60 * 60 * 24;
        Log.d(TAGMA,"one Day:"+ oneDay);
        interval = (today - takenDate) / oneDay;
        return interval + 1;
    }




}