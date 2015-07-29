package com.peacecorps.malaria;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.peacecorps.malaria.R;

import java.util.ArrayList;

/**
 * Created by Ankita on 7/3/2015.
 */
public class TripIndicatorFragmentActivity extends Activity {

    private Button btnInfoHub, btnHome,btnGenerate,btnGear;
    private Spinner drugSpinner,locationSpinner,itemSpinner;
    private String mDrugPicked,mLocationPicked,mItemPicked;
    private EditText cashData,dateData,monthData,yearData;
    public static boolean[] checkSelected;
    private ArrayList<String> items;
    private PopupWindow pw;
    private boolean expanded;
    static SharedPreferenceStore mSharedPreferenceStore;
    private Dialog dialog = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripindicator_layout);
        btnInfoHub=(Button)findViewById(R.id.infoButton);
        btnHome=(Button)findViewById(R.id.homeButton);
        drugSpinner=(Spinner)findViewById(R.id.trip_medication_select_spinner);
        locationSpinner=(Spinner)findViewById(R.id.trip_location_select_spinner);
        //itemSpinner=(Spinner)findViewById(R.id.trip_packing_item_select_spinner);
        btnGenerate=(Button)findViewById(R.id.generateButton);
        btnGear=(Button)findViewById(R.id.trip_settings_button);
        cashData=(EditText)findViewById(R.id.trip_cash_select_editext);
        dateData=(EditText)findViewById(R.id.trip_date);
        monthData=(EditText)findViewById(R.id.trip_month);
        yearData=(EditText)findViewById(R.id.trip_year);



        addListeners();
        createSelectionSpinners();
        initialize();
        createOnItemSelectedListeners();
    }


    public void addListeners(){

        btnGear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                addDialog();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication().getApplicationContext(), MainActivity.class));
            }
        });

        btnInfoHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication().getApplicationContext(), InfoHubFragmentActivity.class));
            }
        });


        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String cash=cashData.getText().toString();
                float cash_value=Float.parseFloat(cash);
                SharedPreferenceStore.mEditor.putFloat("com.peacecorps.malaria.trip_cash_editext",cash_value).commit();
                Log.d("TripIndicatorActivity", "Value for cash: " + cash_value);
                String date_data=dateData.getText().toString();
                int date=Integer.parseInt(date_data);
                String month_data=monthData.getText().toString();
                int month=Integer.parseInt(month_data);
                String year_data=yearData.getText().toString();
                int year=Integer.parseInt(year_data);
                String formattedate=date_data+"/"+month_data+"/"+year_data;
                SharedPreferenceStore.mEditor.putString("com.peacecorps.malaria.trip_date", formattedate).commit();
                String chklist="";
                for(int i=0;i<items.size();i++)
                {
                    if(checkSelected[i]==true)
                    {
                        chklist+=items.get(i)+" ";
                    }

                }
                SharedPreferenceStore.mEditor.putString("com.peacecorps.malaria.trip_packing_items",chklist).commit();
                SharedPreferenceStore.mEditor.putString("com.peacecorps.malaria.trip_drug",mDrugPicked).commit();
                SharedPreferenceStore.mEditor.putString("com.peacecorps.malaria.trip_location",mLocationPicked).commit();

                mItemPicked="Trip to "+mLocationPicked+" is scheduled on "+formattedate+". Please bring "+cash+" in cash and also the following items:- \n"+chklist;

                Toast.makeText(getApplicationContext(),mItemPicked,Toast.LENGTH_LONG).show();

            }
        });

    }

    private void createSelectionSpinners() {

        ArrayAdapter<CharSequence> drugAdapter = ArrayAdapter.createFromResource(
                this, R.array.drug_array,
                R.layout.trip_spinner_item);

        drugAdapter.setDropDownViewResource(R.layout.trip_spinner_popup_item);

        drugSpinner.setAdapter(drugAdapter);

        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(
                this, R.array.location_array,
                R.layout.trip_spinner_item);

        locationAdapter.setDropDownViewResource(R.layout.trip_spinner_popup_item);

        locationSpinner.setAdapter(locationAdapter);

        /*ArrayAdapter<CharSequence> itemAdapter = ArrayAdapter.createFromResource(
                this, R.array.item_array,
                R.layout.trip_spinner_item);

        itemAdapter.setDropDownViewResource(R.layout.trip_spinner_popup_item);

        itemSpinner.setAdapter(itemAdapter);*/


    }

    public void createOnItemSelectedListeners()
    {
        drugSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mDrugPicked = drugSpinner.getItemAtPosition(position).toString();
                Log.d("TripIndicatorActivity", "Chosen ->" + mDrugPicked);
                //Toast.makeText(getApplicationContext(), mDrugPicked, Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mLocationPicked = locationSpinner.getItemAtPosition(position).toString();
                Log.d("TripIndicatorActivity", "Chosen ->" + mLocationPicked);
                //Toast.makeText(getApplicationContext(), mLocationPicked, Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        /*itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mItemPicked = itemSpinner.getItemAtPosition(position).toString();
                Log.d("TripIndicatorActivity", "Chosen ->" + mItemPicked);
                Toast.makeText(getApplicationContext(), mItemPicked, Toast.LENGTH_SHORT).show();

            }

            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });*/



    }

    private void initialize(){

        items = new ArrayList();
        items.add("Drugs");
        items.add("Mosquito Nets");
        items.add("Ointments");

        checkSelected = new boolean[items.size()];
        for (int i = 0; i < checkSelected.length; i++) {
            checkSelected[i] = false;
        }
        Log.d("TripIndicatorActivity", "Items Added, Check Selected Done");
        LinearLayout layout1 = (LinearLayout)findViewById(R.id.tripItemSelector);
        final TextView itemTV=(TextView)findViewById(R.id.tripSelectBox);
        itemTV.setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("TripIndicatorActivity", "Inside itemTV On Click Listener");
                if (!expanded) {
                    //display all selected values
                    String selected = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if (flag == 1)
                        itemTV.setText(selected);
                    expanded = true;
                } else {
                    //display shortened representation of selected values
                    itemTV.setText(DropDownListAdapter.getSelected());
                    expanded = false;
                }
            }
        });
        final Button btnItemDropdown=(Button)findViewById(R.id.tripCreate);
        btnItemDropdown.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp(items, itemTV);
            }
        });

    }

    private void initiatePopUp(ArrayList<String> items, TextView tv){
        LayoutInflater inflater = (LayoutInflater)TripIndicatorFragmentActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.trip_item_dropdown_list, (ViewGroup)findViewById(R.id.tripPackingItemPopUpView));

        //get the view to which drop-down layout is to be anchored
        LinearLayout layout1 = (LinearLayout)findViewById(R.id.tripItemSelector);
        pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });

        //provide the source layout for drop-down
        pw.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.tripPackingItemDropDownList);
        DropDownListAdapter adapter = new DropDownListAdapter(this, items, tv);
        list.setAdapter(adapter);
    }

    public void addDialog()
    {
        dialog = new Dialog(TripIndicatorFragmentActivity.this);
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

                if (ch.equalsIgnoreCase("yes")) {
                    DatabaseSQLiteHelper sqLite = new DatabaseSQLiteHelper(getApplicationContext());
                    sqLite.resetDatabase();
                    mSharedPreferenceStore.mEditor.clear().commit();
                    startActivity(new Intent(getApplication().getApplicationContext(),
                            UserMedicineSettingsFragmentActivity.class));

                } else {
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