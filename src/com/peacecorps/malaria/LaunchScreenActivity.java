package com.peacecorps.malaria;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aneke Chimdindu on 5/28/2014.
 */


// This is just a placeholder screen that captures data from the user preferences from initial setup screen

public class LaunchScreenActivity extends Activity {

    SharedPreferences prefsDrug,prefsTime;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen_layout);


        // Storing the applications data in a shared preferences


        // getting the application SharedPreferences
        prefsDrug  = this.getApplicationContext().getSharedPreferences("com.pc.storeDrugPicked", Context.MODE_PRIVATE);
        prefsTime  = this.getApplicationContext().getSharedPreferences("com.pc.storeTimePicked", Context.MODE_PRIVATE);



        // data retrieval from shared preference
        // if not present, the second parameter value will be returned(null)

        String drugPickedDisplay = prefsDrug.getString("com.pc.drugPicked",null);
        // String timeSet = prefsTime.getString("com.pc.timeSet",null);
        int timeHours = prefsTime.getInt("com.pc.timeHours",5);
        int timeMinutes = prefsTime.getInt("com.pc.timeMinutes",30);





        String timeToRemind = new StringBuilder().append(timeHours).append(':')
                .append(timeMinutes).append(" ").toString();


        Toast.makeText(this.getApplicationContext(),"Your Chosen Drug is : "+drugPickedDisplay +
                ". You will be reminded daily at "+  timeToRemind,Toast.LENGTH_LONG).show();

        TextView userSelectionLabel1 =  (TextView)findViewById(R.id.userSelectionLabel1);
        TextView userSelectionLabel2 =  (TextView)findViewById(R.id.userSelectionLabel2);



        userSelectionLabel1.setText("I am taking :\n" +drugPickedDisplay + " Medication\n");
        userSelectionLabel2.setText("Remind me :\n" +timeToRemind+"\n");


    }

    public void  clearAndExitMethod(View view)
    {

        // clears all stored user preferences in mem
        prefsDrug.edit().clear().commit();
        prefsTime.edit().clear().commit();
        finish();


    }

    public void  exitWithoutClearingMethod(View view)
    {

        finish();

    }

}
