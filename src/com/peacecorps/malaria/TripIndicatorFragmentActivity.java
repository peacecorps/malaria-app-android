package com.peacecorps.malaria;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Ankita on 7/3/2015.
 */
public class TripIndicatorFragmentActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    private Button btnInfoHub, btnHome;
    private Spinner drugSpinner;
    private String mDrugPicked;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripindicator_layout);
        btnInfoHub=(Button)findViewById(R.id.infoButton);
        btnHome=(Button)findViewById(R.id.homeButton);
        drugSpinner=(Spinner)findViewById(R.id.trip_medication_select_spinner);

        addListeners();
        createDrugSelectionSpinner();
    }


    public void addListeners(){

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication().getApplicationContext(),MainActivity.class));
            }
        });

      btnInfoHub.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(getApplication().getApplicationContext(), InfoHubFragmentActivity.class));
          }
      });



    }

    private void createDrugSelectionSpinner() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.user_medicine_settings_activity_drug_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        drugSpinner.setAdapter(adapter);

        drugSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        mDrugPicked = parent.getItemAtPosition(position).toString();
        Log.d("TripIndicatorActivity","Chosen"+mDrugPicked);
        Toast.makeText(getApplicationContext(),mDrugPicked,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}