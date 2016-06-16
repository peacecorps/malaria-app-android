package com.peacecorps.malaria;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yatna on 15/6/16.
 */
public class MedicineStore extends Activity {
    private TextView medicineName;
    private TextView daysLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_store);
        medicineName=(TextView)findViewById(R.id.medicine_name);
        daysLeft=(TextView)findViewById(R.id.days_left);
    }

    public void orderMedicines(View view) {
        Dialog orderMedicineDialog = new Dialog(MedicineStore.this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        orderMedicineDialog.setContentView(R.layout.order_medicine_dialog);

        // Setting Dialog Title
        Button email=(Button)orderMedicineDialog.findViewById(R.id.exitGameDialogButtonOK);
        Button message=(Button)orderMedicineDialog.findViewById(R.id.exitGameDialogButtonCancel);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Showing Alert Message
        orderMedicineDialog.show();
    }

    public void addMedicines(View view) {
        Dialog addMedicineDialog = new Dialog(MedicineStore.this,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        addMedicineDialog.setContentView(R.layout.add_medicine_dialog);

        // Setting Dialog Title
        Button add=(Button)addMedicineDialog.findViewById(R.id.exitGameDialogButtonOK);
        Button cancel=(Button)addMedicineDialog.findViewById(R.id.exitGameDialogButtonCancel);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Showing Alert Message
        addMedicineDialog.show();
    }
}
