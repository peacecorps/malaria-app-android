package com.peacecorps.malaria;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.peacecorps.malaria.R;

import java.util.Date;


public class FirstAnalyticFragment extends Fragment {


    static SharedPreferenceStore mSharedPreferenceStore;
    static View rootView;
    public static TextView checkMediLastTakenTime = null;
    public static TextView doses = null;
    public static TextView adherence = null;
    private Button mSettingsButton;
    private String TAGFAF = "FirstAnalyticFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater
                .inflate(R.layout.fragment_first_analytic_screen, null);


        mSettingsButton = (Button) rootView.findViewById(R.id.fragment_first_screen_settings_button);
        checkMediLastTakenTime = (TextView) rootView.findViewById(R.id.checkMediLastTakenTime);
        doses = (TextView) rootView.findViewById(R.id.doses);
        adherence = (TextView) rootView.findViewById(R.id.adherence);

        if (checkMediLastTakenTime != null) {
            checkMediLastTakenTime.setText(mSharedPreferenceStore.mPrefsStore.getString("com.peacecorps.malaria.checkMediLastTakenTime", "").toString());
            doses.setText("" + mSharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.acceptedCount", 0));
        }

        updateUI();


        return rootView;

    }

    @Override
    public void onResume(){
        updateUI();
        super.onResume();

    }

    public void updateUI(){

        updateAdherence();
        getSharedPreferences();
        addButtonListeners();
    }


    public void addButtonListeners() {
        mSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mSharedPreferenceStore.mEditor.putBoolean(
                        "com.peacecorps.malaria.hasUserSetPreference", false).commit();
                startActivity(new Intent(getActivity(),
                        UserMedicineSettingsFragmentActivity.class));
                getActivity().finish();

            }
        });
    }

    public void getSharedPreferences() {

        mSharedPreferenceStore.mPrefsStore = getActivity()
                .getSharedPreferences("com.peacecorps.malaria.storeTimePicked",
                        Context.MODE_PRIVATE);
        mSharedPreferenceStore.mEditor = mSharedPreferenceStore.mPrefsStore
                .edit();
    }


    public long checkDrugTakenTimeInterval(String time) {
        long interval = 0;
        long today = new Date().getTime();
        //Log.d(TAGMA, "today:" + today);
        long takenDate = SharedPreferenceStore.mPrefsStore.getLong("com.peacecorps.malaria."
                + time, 0);
        //Log.d(TAGMA,"taken date:"+ takenDate);
        long oneDay = 1000 * 60 * 60 * 24;
        //Log.d(TAGMA,"one Day:"+ oneDay);
        interval = (today - takenDate) / oneDay;
        return interval + 1;
    }

    public void updateAdherence(){

        long interval = checkDrugTakenTimeInterval("firstRunTime");
        int takenCount = SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.drugAcceptedCount", 0);
        double adherenceRate;
        Log.d(TAGFAF,"taken Count:"+takenCount);
        //Log.d(TAGMA,""+ interval);
        //Log.d(TAGMA,""+ takenCount);
        if(interval!=0)
            adherenceRate = ((double)takenCount / (double)interval) * 100;
        else
            adherenceRate = 100;
       String ar = String.valueOf(adherenceRate);
        String.format("%.4f",ar);
       adherence.setText(""+adherenceRate);

    }

}