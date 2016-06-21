package com.peacecorps.malaria.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.peacecorps.malaria.R;

/**
 * Created by yatna on 30/5/16.
 */
public class BadgeRoom extends Activity {
    private TextView userScore;
    private ImageView userBadge;
    private SharedPreferences sharedPreferences;
    private int score;
    private Button shareButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.badge_room);
        userScore=(TextView)findViewById(R.id.userScore);
        userBadge=(ImageView)findViewById(R.id.userBadge);
        shareButton=(Button)findViewById(R.id.shareButton);
        displayUserScore();
        displayUserBadge();
        shareButton.setOnClickListener(shareButtonOnClickListener());

    }

    private void displayUserScore(){
        //get user's score
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        score=sharedPreferences.getInt("userScore",0);
        userScore.setText("" + score);
        Log.d("check", "user score displayed");
    }
    private void displayUserBadge() {
        Drawable badge= null;
        //set badge according to score
        if(score<2){
            badge=getResources().getDrawable(R.drawable.y_b1);
        } else if(score<4){
            badge=getResources().getDrawable(R.drawable.y_b3);
        }else if(score<6){
            badge=getResources().getDrawable(R.drawable.y_b5);
        }else if(score<7){
            badge=getResources().getDrawable(R.drawable.y_b6);
        }else if(score<8){
            badge=getResources().getDrawable(R.drawable.y_newbie);
        }else{
            badge=getResources().getDrawable(R.drawable.y_hat);
        }
        //display the badge
        userBadge.setImageDrawable(badge);
    }
    private View.OnClickListener shareButtonOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Yatna unlocked a new badge 'Champ'!! Score : " +userScore);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share Via..."));
            }
        };
    }
}
