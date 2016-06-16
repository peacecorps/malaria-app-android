package com.peacecorps.malaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by yatna on 14/6/16.
 */
public class NewHomeActivity extends Activity{
    private Button badgeScreenButton;
    private Button mythFactButton;
    private Button rapidFireButton;
    private Button medicineStoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_home_activity);
        badgeScreenButton=(Button)findViewById(R.id.badgeScreen);
        rapidFireButton=(Button)findViewById(R.id.rapidFire);
        mythFactButton=(Button)findViewById(R.id.mythFact);
        medicineStoreButton=(Button)findViewById(R.id.medicineStore);
        badgeScreenButton.setOnClickListener(badgeScreenOnClickListener());
        mythFactButton.setOnClickListener(mythFactGameOnClickListener());
        rapidFireButton.setOnClickListener(rapidFireButtonOnClickListener());
        medicineStoreButton.setOnClickListener(medicineStoreOnClickListener());
    }

    private View.OnClickListener medicineStoreOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewHomeActivity.this,MedicineStore.class));
            }
        };
    }

    private View.OnClickListener badgeScreenOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewHomeActivity.this, BadgeRoom.class));
            }
        };
    }

    private View.OnClickListener mythFactGameOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewHomeActivity.this, MythFactGame.class));
            }
        };
    }

    private View.OnClickListener rapidFireButtonOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewHomeActivity.this, RapidFireGame.class));
            }
        };
    }
}
