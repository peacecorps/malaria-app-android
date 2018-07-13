package com.peacecorps.malaria.ui.home_screen;


import android.os.Bundle;

import com.peacecorps.malaria.R;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.peacecorps.malaria.code.fragment.HomeScreenFragment;
import com.peacecorps.malaria.data.AppDataManager;
import com.peacecorps.malaria.ui.base.BaseActivity;
import com.peacecorps.malaria.ui.user_profile.UserProfileFragment;
import com.peacecorps.malaria.utils.BottomNavigationViewHelper;
import com.peacecorps.malaria.utils.InjectionClass;

public class MainActivity extends BaseActivity implements HomeContract.IHomeView, UserProfileFragment.OnUserFragmentListener {

    private HomePresenter<MainActivity> presenter;
    private BottomNavigationView bottomNavigationView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void init() {
        // setting up presenter and attaching views
        AppDataManager dataManager = InjectionClass.provideDataManager(this);
        presenter = new HomePresenter<>(dataManager, this);
        presenter.attachView(this);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }

        //setting up bottom navigation
        setBottomNavigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    private void setBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.home_screen_fragment);
        bottomNavigationView.getMenu().findItem(R.id.bnv_home).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.bnv_home:
                        fragment = new HomeScreenFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.bnv_trip_button:
                        Toast.makeText(MainActivity.this, "trip button", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bnv_info_button:
                        Toast.makeText(MainActivity.this, "info button", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bnv_play_button:
                        Toast.makeText(MainActivity.this, "play button", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.bnv_user_profile:
                        fragment = new UserProfileFragment();
                        loadFragment(fragment);
                        break;
                    default:
                        fragment = new HomeScreenFragment();
                        loadFragment(fragment);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void startHomeFragment() {
        bottomNavigationView.setSelectedItemId(R.id.home_screen_fragment);
        loadFragment(new HomeScreenFragment());
    }
}
