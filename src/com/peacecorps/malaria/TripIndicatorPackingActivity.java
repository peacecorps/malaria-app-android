package com.peacecorps.malaria;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Ankita on 8/5/2015.
 */
public class TripIndicatorPackingActivity extends ListActivity {

    private long mNumDrugs=0;
    TextView numDrugs;
    ListView listView;
    EditText cash;

    /** Items entered by the user is stored in this ArrayList variable */
    ArrayList<String> list = new ArrayList<String>();

    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter<String> adapter;

    private SharedPreferenceStore mSharedPreferenceStore;

    private String [] outputStrArr;

    private Intent bkIntent;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DatabaseSQLiteHelper sqLite = new DatabaseSQLiteHelper(this);

        /** Setting a custom layout for the list activity */
        setContentView(R.layout.trip_indicator_packing_dialog);

        /** Reference to the button of the layout main.xml */
        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        ImageButton btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        ImageButton btnSubmit = (ImageButton) findViewById(R.id.btnSubmit);

        /** Cash Data**/
        cash=(EditText)findViewById(R.id.cash_et);

        /** List View **/
        listView = (ListView)findViewById(android.R.id.list);

        /** Defining the ArrayAdapter to set items to ListView */
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, list);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        bkIntent = new Intent(getApplicationContext(),
                TripIndicatorFragmentActivity.class);


        /** Defining a click event listener for the button "Add" */
        View.OnClickListener listenerAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.packing_et);
                list.add(edit.getText().toString());
                edit.setText("");
                adapter.notifyDataSetChanged();
            }
        };

        /** Setting the event listener for the add button */
        btnAdd.setOnClickListener(listenerAdd);

        /** Setting the adapter to the ListView */
        setListAdapter(adapter);

        View.OnClickListener listenerDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** Getting the checked items from the listview */
                SparseBooleanArray checkedItemPositions = getListView().getCheckedItemPositions();
                int itemCount = getListView().getCount();

                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.get(i)){
                        adapter.remove(list.get(i));
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();
            }
        };

        /** Setting the event listener for the add button */
        btnDelete.setOnClickListener(listenerDelete);

        View.OnClickListener listenerSubmit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checked = getListView().getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    // Add item if it is checked i.e.) == TRUE!
                    if (checked.valueAt(i))
                        selectedItems.add(adapter.getItem(position));
                }

                outputStrArr = new String[selectedItems.size()];

                for (int i = 0; i < selectedItems.size(); i++) {
                    outputStrArr[i] = selectedItems.get(i);
                    sqLite.insertPackingItem(outputStrArr[i],1);
                }

                getSharedPreferences();

                mSharedPreferenceStore.mEditor.putInt("Array Size", outputStrArr.length);

                for(int i=0;i<outputStrArr.length;i++)
                    mSharedPreferenceStore.mEditor.putString(outputStrArr + "_" + i, outputStrArr[i]).commit();

                // Create a bundle object
                Bundle b = new Bundle();
                b.putStringArray("selectedItems", outputStrArr);

                // Add the bundle to the intent.
                bkIntent.putExtras(b);
                //.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                int cashV=0;
                try {
                    cashV=Integer.parseInt(cash.getText().toString());
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Enter Integral Value of Cash!",Toast.LENGTH_SHORT);
                }


                sqLite.insertPackingItem("Cash", cashV);

                startActivity(bkIntent);

                TripIndicatorPackingActivity.this.finish();

            }
        };

        /** Setting the event listener for the add button */
        btnSubmit.setOnClickListener(listenerSubmit);



        Intent intent = getIntent();
        mNumDrugs=intent.getLongExtra(TripIndicatorFragmentActivity.DRUG_TAG,0);
        numDrugs = (TextView)findViewById(R.id.quantity);
        numDrugs.setText("" + mNumDrugs);
        sqLite.insertPackingItem("Drug",(int)mNumDrugs);

    }

    private void getSharedPreferences() {
        // reading the application SharedPreferences for storing of time and
        // drug selected
        mSharedPreferenceStore.mPrefsStore = getSharedPreferences(
                "com.peacecorps.malaria.packingItem", Context.MODE_PRIVATE);
        mSharedPreferenceStore.mEditor = mSharedPreferenceStore.mPrefsStore
                .edit();
    }






}
