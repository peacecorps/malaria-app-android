package com.peacecorps.malaria.ui.home_screen;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.peacecorps.malaria.R;
import com.peacecorps.malaria.code.fragment.HomeScreenFragment;
import com.peacecorps.malaria.data.AppDataManager;
import com.peacecorps.malaria.data.db.entities.Packing;
import com.peacecorps.malaria.ui.base.BaseActivity;
import com.peacecorps.malaria.ui.home_screen.HomeContract.IHomeView;
import com.peacecorps.malaria.ui.info_hub.InfoHubFragment;
import com.peacecorps.malaria.ui.play.PlayFragment;
import com.peacecorps.malaria.ui.play.PlayFragment.OnPlayFragmentListener;
import com.peacecorps.malaria.ui.play.badge_screen.BadgeScreenFragment;
import com.peacecorps.malaria.ui.play.medicine_store.MedicineStoreFragment;
import com.peacecorps.malaria.ui.play.myth_vs_fact.MythFactFragment;
import com.peacecorps.malaria.ui.play.rapid_fire.RapidFireFragment;
import com.peacecorps.malaria.ui.play.rapid_fire.RapidFireFragment.OnRapidFragmentListener;
import com.peacecorps.malaria.ui.trip_reminder.PlanTripFragment;
import com.peacecorps.malaria.ui.trip_reminder.PlanTripFragment.OnPlanFragmentListener;
import com.peacecorps.malaria.ui.trip_reminder.trip_select_item.ItemDialogFragment;
import com.peacecorps.malaria.ui.trip_reminder.trip_select_item.ItemDialogFragment.OnSaveDialogListener;
import com.peacecorps.malaria.ui.user_medicine_setting.MedicineSettingsActivity;
import com.peacecorps.malaria.ui.user_profile.UserProfileFragment;
import com.peacecorps.malaria.ui.user_profile.UserProfileFragment.OnUserFragmentListener;
import com.peacecorps.malaria.utils.BottomNavigationViewHelper;
import com.peacecorps.malaria.utils.InjectionClass;
import com.peacecorps.malaria.utils.ToastLogSnackBarUtil;

import static com.peacecorps.malaria.ui.play.myth_vs_fact.MythFactFragment.OnMythFragmentListener;

public class MainActivity extends BaseActivity implements IHomeView, OnUserFragmentListener,
        OnPlayFragmentListener, OnMythFragmentListener, OnRapidFragmentListener, OnPlanFragmentListener,OnSaveDialogListener {

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

        Toolbar mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        // adding ic_launcher icon to the Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }

        //setting up bottom navigation
        setBottomNavigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // detach view & make presenter null
        presenter.detachView();
        presenter = null;
    }

    private void setBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // disables moving/shifting mode (by default available for >3 items in bottom navigation) for application
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // set home icon to be default selected & add Home screen fragment in Frame layout
        bottomNavigationView.setSelectedItemId(R.id.home_screen_fragment);
        bottomNavigationView.getMenu().findItem(R.id.bnv_home).setChecked(true);
        Fragment fragment = new HomeScreenFragment();
        loadFragment(fragment);

        // listener implementation for bottom navigation, replaces frame layout with different fragments
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
                        fragment = new PlanTripFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.bnv_info_button:
                        fragment = new InfoHubFragment();
                        loadFragment(fragment);
                        break;

                    case R.id.bnv_play_button:
                        fragment = new PlayFragment();
                        loadFragment(fragment);
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

    /**
     * @param fragment : replaces frame layout with parameter received in main activity
     */
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, "");
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    // reset button to create a dialog & if clicked okay, delete all data & start User medicine settings activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btn_menu_reset) {
            // creating dialog to display
            final Dialog dialog;
            dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
            dialog.setContentView(R.layout.resetdata_dialog);
            dialog.setTitle("Reset Data");
            // reset database & dismiss dialog
            dialog.findViewById(R.id.btn_dialog_reset_okay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });
            // dismiss dialog
            dialog.findViewById(R.id.btn_dialog_reset_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // inflates the main_menu which contains "reset button for action"
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // starts homes fragment
    @Override
    public void startHomeFragment() {
        bottomNavigationView.setSelectedItemId(R.id.home_screen_fragment);
        loadFragment(new HomeScreenFragment());
    }

    /**
     * @param id : Checks id received (button IDs in playFragment, loads respective fragment)
     */
    @Override
    public void replacePlayFragment(int id) {
        switch (id) {
            case R.id.btn_badge_screen:
                loadFragment(new BadgeScreenFragment());
                break;
            case R.id.btn_myth_vs_fact:
                loadFragment(new MythFactFragment());
                break;
            case R.id.btn_medicine_store:
                loadFragment(new MedicineStoreFragment());
                break;

            case R.id.btn_rapid_fire:
                loadFragment(new RapidFireFragment());
                break;
            default:
                ToastLogSnackBarUtil.showToast(this, "Wrong button ");
        }
    }

    // starts playFragment from another child fragment
    @Override
    public void goBackToPlayFragment() {
        loadFragment(new PlayFragment());
    }

    // replaces trip fragment with select item fragment
    @Override
    public void startSelectItemFragment(long quantity) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ItemDialogFragment newFragment = ItemDialogFragment.newInstance(quantity);

        // using a large layout, so show the fragment as a dialog
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null).commit();

    }

    @Override
    public void passMedicineSelected(Packing packing) {
        Fragment fragmentInFrame = getSupportFragmentManager()
                .findFragmentById(R.id.frame_container);
        if(fragmentInFrame instanceof PlanTripFragment) {
            PlanTripFragment tripFragment = (PlanTripFragment) fragmentInFrame;
            tripFragment.updateSelectItemText(packing.getPackingItem() + ":" + packing.getPackingQuantity());
        }
    }

    @Override
    public void startMedicineSettingActivity() {
        Intent intent = new Intent(this, MedicineSettingsActivity.class);
        startActivity(intent);
    }
}
