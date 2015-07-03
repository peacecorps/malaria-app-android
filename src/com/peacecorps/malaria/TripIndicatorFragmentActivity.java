package com.peacecorps.malaria;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ankita on 7/3/2015.
 */
public class TripIndicatorFragmentActivity extends FragmentActivity {

    private Button btnInfoHub, btnHome;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripindicator_layout);
        btnInfoHub=(Button)findViewById(R.id.infoButton);
        btnHome=(Button)findViewById(R.id.homeButton);
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
              startActivity(new Intent(getApplication().getApplicationContext(),InfoHubFragmentActivity.class));
          }
      });



    }
}