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
public  class InitialScreenActivity extends Activity implements AdapterView.OnItemSelectedListener {
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
    SharedPreferences prefsStore;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // sets this activity's screen to a layout called from the res/layout initial_screen_layout.xml
        setContentView(R.layout.initial_screen_layout);
        boolean isDoneButtonChecked =false;
        doneButton = (Button) findViewById(R.id.doneButton);
        timePickButton = (TextView) findViewById(R.id.timePickButton);
        drugTakeLabel  = (TextView) findViewById(R.id.drugTakeLabel);
        setupLabel = (TextView)findViewById(R.id.setupLabel);
        timePickLabel = (TextView)findViewById(R.id.timePickLabel);
        ifForgetLabel = (TextView)findViewById(R.id.ifForgetLabel);
        drugSelectSpinner = (Spinner) findViewById(R.id.drugSelectSpinner);

        getSharedPreferences();

        // call function that sets colors for text colors on layout components and widgets
        setLayoutColors();

        // Condition to show this screen only on first installation of app or
        // when the user  has cleared or reinstalled this app, or through the settings button
        checkInitialAppInstall();
        //calls function that creates the drug selection spinner
        createDrugSelectionSpinner();

        //keep Done Button disabled untill the user sets time for the app to remind him
        checkIfTimeSet(isDoneButtonChecked);

        // Calling Time Pick Button ClickListener Method
        addTimePickButtonClickListener();

        // Calling Done Button ClickListener Method
        addDoneButtonClickListener();
    }

    public void checkIfTimeSet(boolean isDoneButtonChecked){
        doneButton.setEnabled(isDoneButtonChecked);
    }

    // function that sets the text colors of layout components and widgets
    private void setLayoutColors() {
        // Text Color settings for labels and button texts for the screen
        setupLabel.setHintTextColor(Color.rgb(89, 43, 21));
        doneButton.setHintTextColor(Color.rgb(89, 43, 21));
        drugTakeLabel.setHintTextColor(Color.rgb(102, 74, 58));
        timePickLabel.setHintTextColor(Color.rgb(102, 74, 58));
        ifForgetLabel.setHintTextColor(Color.rgb(102, 74, 58));

    }

    //function that creates the drug selection spinner
    private void createDrugSelectionSpinner() {
        // Created an array adapter from the string xml file (res/values/strings.xml) to store the drugs for user selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.drug_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of drug choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the drug selection Spinner
        drugSelectSpinner.setAdapter(adapter);

        //activating the listener for this spinner
        drugSelectSpinner.setOnItemSelectedListener(this);
        }

    public void getSharedPreferences() {
        // reading  the application SharedPreferences for storing of time and drug selected
        prefsStore = this.getApplicationContext().getSharedPreferences("com.pc.storeTimePicked", Context.MODE_PRIVATE);
        editor = prefsStore.edit();
    }

     private void checkInitialAppInstall()
    {
        //checking if this app has been installed before
        if (prefsStore.getBoolean("com.pc.checkedPreference", false)) {
            Intent intent = new Intent(this, LaunchScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void showHomeScreen() {
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
            boolean isDoneButtonChecked = true;

            //setting the picked hour and minute from the pickerDialog
            hour   = hourOfDay;
            minute = minutes;

            updateTime(hour,minute);
            checkIfTimeSet(isDoneButtonChecked);
            getTimePrefs();
        }

    };

    // method for storing time of day preferences of a user
    public void getTimePrefs(){
        // Storing the applications time of day data selected in a shared preference
        editor.putInt("com.pc.timeHours",hour);
        editor.putInt("com.pc.timeMinutes",minute);

        editor.putBoolean("com.pc.checkedPreference",true);
        editor.commit();
    }
    // converts 24hr format to 12hr format with AM/PM values
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

        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append the time to a stringBuilder
        String theTime = String.valueOf(hours) + ':' + minutes + " " + timeSet;

        // Set the timePickButton as the converted time
        timePickButton.setText(theTime);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // retrieving an item that was selected
        String drugPicked  =  parent.getItemAtPosition(position).toString();

        //setting the spinner with the currently selected item
        parent.setSelection(parent.getSelectedItemPosition());

        // Storing the applications drug selected  data in a shared preferences
        editor.putString("com.pc.drugPicked",drugPicked);
        editor.putBoolean("com.pc.checkedPreference",true);
        editor.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}


