package com.peacecorps.malaria;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

/**
 * @author Aneke Chimdindu
 * @version 1.0
 * App Name: Malaria Prevention App for Systers Peace Corps Volunteer
 *
 */


public class InitialScreenActivity extends Activity implements AdapterView.OnItemSelectedListener {
    /**
     * Called when the activity is first created.
     */




    private TextView timePickButton;
    private TextView setupLabel;
    private TextView drugTakeLabel;
    private TextView timePickLabel;
    private TextView ifForgetLabel;
    private Spinner drugSelectSpinner;
    private Button doneButton;
    private int hour;
    private int minute;
    static final int TIME_OF_DAY_DIALOG_ID = 101;


    SharedPreferences prefsTime;
    SharedPreferences prefsDrug;
    SharedPreferences.Editor editor;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // sets this activity's screen to a layout called from the res/layout initial_screen_layout.xml
        setContentView(R.layout.initial_screen_layout);


        doneButton = (Button) findViewById(R.id.doneButton);

        timePickButton = (TextView) findViewById(R.id.timePickButton);

        drugTakeLabel  = (TextView) findViewById(R.id.drugTakeLabel);

        setupLabel = (TextView)findViewById(R.id.setupLabel);

        timePickLabel = (TextView)findViewById(R.id.timePickLabel);

        ifForgetLabel = (TextView)findViewById(R.id.ifForgetLabel);

        drugSelectSpinner = (Spinner) findViewById(R.id.drugSelectSpinner);


        // Text Color settings for labels and button texts for the screen
        setupLabel.setHintTextColor(Color.rgb(89, 43, 21));

        doneButton.setHintTextColor(Color.rgb(89, 43, 21));

        drugTakeLabel.setHintTextColor(Color.rgb(102, 74, 58));

        timePickLabel.setHintTextColor(Color.rgb(102,74,58));

        ifForgetLabel.setHintTextColor(Color.rgb(102,74,58));


        //reading from  stored user time SharedPreference to use in the app first install condition below
        SharedPreferences checkInitialPersist =  getSharedPreferences("com.pc.storeTimePicked", Context.MODE_PRIVATE);


        // Condition to show this screen only on first installation of app or
        // when the user  has cleared or reinstalled this app, or through the settings button
        if(checkInitialPersist.getBoolean("com.pc.checkedPreference",false))
        {
            Intent intent  = new Intent(this,LaunchScreenActivity.class);
            startActivity(intent);
            finish();
        }







        // Created an array adapter from the string xml file (res/values/strings.xml) to store the drugs for user selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drug_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of drug choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the drug selection Spinner
        drugSelectSpinner.setAdapter(adapter);

        //activating the listener for this spinner
        drugSelectSpinner.setOnItemSelectedListener(this);



        //keep DOne Button disabled untill the user sets time for the app to remind him
        if(timePickButton.getText().toString().equals("Pick Time"))
        {
            doneButton.setEnabled(false);
        }








        // Calling Time Pick Button ClickListener Method
        addTimePickButtonClickListener();


        // Calling Done Button ClickListener Method
        addDoneButtonClickListener();




    }






    public void showHomeScreen() {
        Intent intent = new Intent(this, LaunchScreenActivity.class);


        startActivity(intent);

        finish();
    }



    public void addDoneButtonClickListener() {

        doneButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                showHomeScreen();

            }

        });

    }



    public void addTimePickButtonClickListener() {



        timePickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_OF_DAY_DIALOG_ID);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_OF_DAY_DIALOG_ID:

                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;



            updateTime(hour,minute);
            doneButton.setEnabled(true);
            getTimePrefs();


        }

    };


    // method for storing time of day preferences of a user
    public void getTimePrefs()
    {
        // reading  the application SharedPreferences for time of day
        prefsTime  = this.getApplicationContext().getSharedPreferences("com.pc.storeTimePicked", Context.MODE_PRIVATE);
        editor = prefsTime.edit();

        // Storing the applications time of day data selected in a shared preference
        editor.putInt("com.pc.timeHours",hour);
        editor.putInt("com.pc.timeMinutes",minute);

        editor.putBoolean("com.pc.checkedPreference",true);

        editor.commit();

    }



    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {


        String  timeSet;
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


        String minutes                        ;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // AAppend the time to a stringBuilder
        String theTime = String.valueOf(hours) + ':' + minutes + " " + timeSet;


        // Set the timePickButton as the converted time
        timePickButton.setText(theTime);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        String drugPicked  =  parent.getItemAtPosition(position).toString();

        parent.setSelection(parent.getSelectedItemPosition());

        //Toast.makeText(this.getApplicationContext() ,drugPicked,Toast.LENGTH_SHORT).show();

        // getting the application drug selected  SharedPreferences
        prefsDrug  = this.getApplicationContext().getSharedPreferences("com.pc.storeDrugPicked", Context.MODE_PRIVATE);
        editor = prefsDrug.edit();



        // Storing the applications drug selected  data in a shared preferences
        editor.putString("com.pc.drugPicked",drugPicked);
        editor.putBoolean("com.pc.checkedPreference",true);
        editor.commit();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        // Another interface callback i
    }
}


