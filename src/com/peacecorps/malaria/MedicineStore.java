package com.peacecorps.malaria;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yatna on 15/6/16.
 */
public class MedicineStore extends Activity {
    private TextView medicineName;
    private TextView daysLeft;
    private Button addMedicine;
    private Button orderMedicine;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int medicineStore;
    private String drugName;
    static SharedPreferenceStore mSharedPreferenceStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_store);
        medicineName=(TextView)findViewById(R.id.medicine_name);
        daysLeft=(TextView)findViewById(R.id.days_left);
        addMedicine=(Button)findViewById(R.id.add_medicine);
        orderMedicine=(Button)findViewById(R.id.order_medicine);
        addMedicine.setOnClickListener(addMedicinesOnClickListener());
        orderMedicine.setOnClickListener(orderMedicineOnClickListener());
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferenceStore = new SharedPreferenceStore();
        mSharedPreferenceStore.getSharedPreferences(this);
        drugName=SharedPreferenceStore.mPrefsStore.getString("com.peacecorps.malaria.drugPicked", null);
        medicineName.setText(drugName);
        displayMedicineStore();
    }
    private void displayMedicineStore(){
        medicineStore=preferences.getInt("medicineStore",0);
        if(medicineStore>=0){
            daysLeft.setText(medicineStore+" Days");
        }
        else{
            medicineStore=medicineStore*-1;
            daysLeft.setText(medicineStore+" Days Due");
        }
    }
    public View.OnClickListener orderMedicineOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog orderMedicineDialog = new Dialog(MedicineStore.this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
                orderMedicineDialog.setContentView(R.layout.order_medicine_dialog);

                // Setting Dialog Title
                Button email=(Button)orderMedicineDialog.findViewById(R.id.order_by_email);
                Button message=(Button)orderMedicineDialog.findViewById(R.id.order_by_msg);
                EditText medicineQuantityEt=(EditText)orderMedicineDialog.findViewById(R.id.order_quantity);
                ((EditText)orderMedicineDialog.findViewById(R.id.order_quantity)).requestFocus();


                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(medicineQuantityEt.getText().toString().trim().equals("")){
                            medicineQuantityEt.setError("Quantity Required");
                        }
                        else{
                            //send and email
                            String msgBody="My malaria pills will last for the coming  "+"<b>"+medicineStore+"</b>"+" days only.<br> Send the following immediately: <br>" +
                                    "Medicine Name:     " +"<b>"+drugName+"</b>"+"<br>"+
                                    "Quantity:          " ;
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"yatnavermaa@gmail.com", "yatna.verma.ece13@itbhu.ac.in"});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "URGENT: Reqiured Malaria Medicines");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(msgBody+"<b>"+Integer.parseInt(medicineQuantityEt.getText().toString())+"</b>"));
                            startActivity(Intent.createChooser(emailIntent, "Send mail via..."));
                            orderMedicineDialog.dismiss();
                        }
                    }
                });
                message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(medicineQuantityEt.getText().toString().trim().equals("")){
                            medicineQuantityEt.setError("Quantity Required");
                        }
                        else{
                            String msgBody="My malaria pills will last for the coming "+medicineStore+" days only.\n Send the following immediately: \n" +
                                    "Medicine Name:     " +drugName+"\n"+
                                    "Quantity:          " ;
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage("123", null,msgBody + Integer.parseInt(medicineQuantityEt.getText().toString()), null, null);
                            Toast.makeText(MedicineStore.this, "SMS sent.", Toast.LENGTH_SHORT).show();
                            orderMedicineDialog.dismiss();
                        }
                    }
                });

                // Showing Alert Message
                orderMedicineDialog.show();
            }
        };
    }

    public View.OnClickListener addMedicinesOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog addMedicineDialog = new Dialog(MedicineStore.this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
                addMedicineDialog.setContentView(R.layout.add_medicine_dialog);

                // Setting Dialog Title
                Button add=(Button)addMedicineDialog.findViewById(R.id.add_medicine_dialog);
                Button cancel=(Button)addMedicineDialog.findViewById(R.id.cancel_medicine_dialog);
                EditText medicineQuantityEt=(EditText)addMedicineDialog.findViewById(R.id.add_medicine_quantity);
                ((EditText)addMedicineDialog.findViewById(R.id.add_medicine_quantity)).requestFocus();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(medicineQuantityEt.getText().toString().trim().equals("")){
                            medicineQuantityEt.setError("Quantity Required");
                        }
                        else{
                            medicineStore=preferences.getInt("medicineStore",0);
                            editor=preferences.edit();
                            int quantity = Integer.parseInt(medicineQuantityEt.getText().toString());
                            editor.putInt("medicineStore",medicineStore+quantity);
                            editor.commit();
                            displayMedicineStore();
                            addMedicineDialog.dismiss();
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addMedicineDialog.dismiss();
                    }
                });

                // Showing Alert Message
                addMedicineDialog.show();

            }
        };

    }
}
