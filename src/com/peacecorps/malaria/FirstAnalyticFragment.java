package com.peacecorps.malaria;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.peacecorps.malaria.R;

<<<<<<< HEAD
import java.util.Calendar;
=======
>>>>>>> ankita-gsoc-gradlebuild
import java.util.Date;


public class FirstAnalyticFragment extends Fragment {


    static SharedPreferenceStore mSharedPreferenceStore;
    static View rootView;
    public static TextView checkMediLastTakenTime = null;
    public static TextView doses = null;
    public static TextView adherence = null;
    private Button mSettingsButton;
    private String TAGFAF = "FirstAnalyticFragment";
    private Dialog dialog = null;
<<<<<<< HEAD
    private TextView mlt,dinr,atm;
=======
>>>>>>> ankita-gsoc-gradlebuild


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater
                .inflate(R.layout.fragment_first_analytic_screen, null);


        mSettingsButton = (Button) rootView.findViewById(R.id.fragment_first_screen_settings_button);
        checkMediLastTakenTime = (TextView) rootView.findViewById(R.id.checkMediLastTakenTime);
        doses = (TextView) rootView.findViewById(R.id.doses);
        adherence = (TextView) rootView.findViewById(R.id.adherence);
        updateUI();
<<<<<<< HEAD
        mlt=(TextView)rootView.findViewById(R.id.mlt);
        dinr=(TextView)rootView.findViewById(R.id.dinr);
        atm=(TextView)rootView.findViewById(R.id.atm);

        Typeface cf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/garreg.ttf");
        Typeface cfb = Typeface.createFromAsset(getActivity().getAssets(), "fonts/garbold.ttf");
        mlt.setTypeface(cf);
        dinr.setTypeface(cf);
        atm.setTypeface(cf);

=======


        return rootView;

    }
>>>>>>> ankita-gsoc-gradlebuild

    @Override
    public void onResume(){
        updateUI();
        super.onResume();

    }

<<<<<<< HEAD
    @Override
    public void onResume(){
        updateUI();
        super.onResume();

    }

=======
>>>>>>> ankita-gsoc-gradlebuild
    public void updateUI(){


        updateMediLastTime();
        Log.d(TAGFAF,"AFTER CHK MEDI LAST TIME");
        if (checkMediLastTakenTime != null)
            updateDoses();
        Log.d(TAGFAF, "AFTER UPDATE DOSES");
        updateAdherence();
        Log.d(TAGFAF, "AFTER UPDATE ADHERENCE");
        getSharedPreferences();
        Log.d(TAGFAF, "AFTER SHARED PREFS");
        addButtonListeners();
        Log.d(TAGFAF, "AFTER BUTTON LISTENERS");
    }


    public void addButtonListeners() {
        mSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

<<<<<<< HEAD
=======
                mSharedPreferenceStore.mEditor.putBoolean(
                        "com.peacecorps.malaria.hasUserSetPreference", false).commit();
>>>>>>> ankita-gsoc-gradlebuild
                addDialog();

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
<<<<<<< HEAD
        Date tdy= Calendar.getInstance().getTime();
        tdy.setTime(today);
=======
>>>>>>> ankita-gsoc-gradlebuild
        DatabaseSQLiteHelper sqLite= new DatabaseSQLiteHelper(getActivity());
        long takenDate= sqLite.getFirstTime();
        if(time.compareTo("firstRunTime")==0) {
            if(takenDate!=0) {
                Log.d(TAGFAF, "First Run Time at FAF->" + takenDate);
<<<<<<< HEAD
                Calendar cal=Calendar.getInstance();
                cal.setTimeInMillis(takenDate);
                cal.add(Calendar.MONTH, 1);
                Date start=cal.getTime();
                int weekDay=cal.get(Calendar.DAY_OF_WEEK);
                if(SharedPreferenceStore.mPrefsStore.getBoolean("com.peacecorps.malaria.isWeekly",false))
                    interval=sqLite.getIntervalWeekly(start,tdy,weekDay);
                else
                    interval=sqLite.getIntervalDaily(start,tdy);
                SharedPreferenceStore.mEditor.putLong("com.peacecorps.malaria."
                        + time, takenDate).apply();
                /*long oneDay = 1000 * 60 * 60 * 24;
                interval = (today - takenDate) / oneDay;*/
=======
                SharedPreferenceStore.mEditor.putLong("com.peacecorps.malaria."
                        + time, takenDate).apply();
                long oneDay = 1000 * 60 * 60 * 24;
                interval = (today - takenDate) / oneDay;
                Log.d(TAGFAF, "TODAY:" + today);
                Log.d(TAGFAF,"TAKEN DATE"+takenDate);
                Log.d(TAGFAF,"INTERVAL:"+interval);
>>>>>>> ankita-gsoc-gradlebuild
                return interval;
            }
            else
                return 1;
        }
        else {
            takenDate=SharedPreferenceStore.mPrefsStore.getLong("com.peacecorps.malaria."
                    + time, takenDate);
            long oneDay = 1000 * 60 * 60 * 24;
            interval = (today - takenDate) / oneDay;
            return interval;
        }
    }

    public void updateAdherence(){

        long interval = checkDrugTakenTimeInterval("firstRunTime");
<<<<<<< HEAD
        DatabaseSQLiteHelper sqLite = new DatabaseSQLiteHelper(getActivity());
        long takenCount = sqLite.getCountTaken();
        double adherenceRate;
        Log.d(TAGFAF,"taken Count:"+takenCount);
        //Log.d(TAGMA,""+ interval);
=======
        int takenCount = SharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.drugAcceptedCount", 0);
        double adherenceRate;
        Log.d(TAGFAF,"taken Count:"+takenCount);
        Log.d(TAGFAF,"INTERVAL:"+ interval);
>>>>>>> ankita-gsoc-gradlebuild
        //Log.d(TAGMA,""+ takenCount);
        if(interval!=1)
            adherenceRate = ((double)takenCount / (double)interval) * 100;
        else
            adherenceRate = 100;

        String ar = String.format("%.2f %%", adherenceRate);
        Log.d(TAGFAF,"Adherence Rate:"+ar);
<<<<<<< HEAD
        //adherence.setText(ar);
=======
        adherence.setText(ar);
>>>>>>> ankita-gsoc-gradlebuild

    }

    public void updateDoses()
    {   DatabaseSQLiteHelper sqLite = new DatabaseSQLiteHelper(getActivity());
        Log.d(TAGFAF, "INSIDE updateDoses");
        if(mSharedPreferenceStore.mPrefsStore.getBoolean("com.peacecorps.malaria.isWeekly",false)) {
            int d = sqLite.getDosesInaRowWeekly();
            mSharedPreferenceStore.mEditor.putInt("com.peacecorps.malaria.weeklyDose", d).apply();
<<<<<<< HEAD
            doses.setText("" + d/*mSharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.weeklyDose", 0)*/);
=======
            doses.setText("" + mSharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.weeklyDose", 0));
>>>>>>> ankita-gsoc-gradlebuild
        }
        else
        {
            int d = sqLite.getDosesInaRowDaily();
            mSharedPreferenceStore.mEditor.putInt("com.peacecorps.malaria.dailyDose",d).apply();
            doses.setText("" + mSharedPreferenceStore.mPrefsStore.getInt("com.peacecorps.malaria.dailyDose", 0));
        }
    }

    public void updateMediLastTime() {
        if (checkMediLastTakenTime != null) {
            checkMediLastTakenTime.setText(mSharedPreferenceStore.mPrefsStore.getString("com.peacecorps.malaria.checkMediLastTakenTime", "").toString());
        }
    }

    public void addDialog()
    {
<<<<<<< HEAD
        dialog = new Dialog(this.getActivity(),android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
=======
        dialog = new Dialog(this.getActivity());
>>>>>>> ankita-gsoc-gradlebuild
        dialog.setContentView(R.layout.resetdata_dialog);
        dialog.setTitle("Reset Data");

        final RadioGroup btnRadGroup = (RadioGroup) dialog.findViewById(R.id.radioGroupReset);
        Button btnOK = (Button) dialog.findViewById(R.id.dialogButtonOKReset);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = btnRadGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton btnRadButton = (RadioButton) dialog.findViewById(selectedId);

                String ch = btnRadButton.getText().toString();

                if(ch.equalsIgnoreCase("yes"))
                {
                    DatabaseSQLiteHelper sqLite = new DatabaseSQLiteHelper(getActivity());
                    sqLite.resetDatabase();
                    mSharedPreferenceStore.mEditor.clear().commit();
                    startActivity(new Intent(getActivity(),
                            UserMedicineSettingsFragmentActivity.class));
                    getActivity().finish();
                }
                else
                {
                    dialog.dismiss();
                }

            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.dialogButtonCancelReset);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}

