package com.peacecorps.malaria;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by DELL on 1/16/2016.
 */
public class RemainderToneActivity extends Activity implements View.OnClickListener{

    Button btnBrowse;
    EditText path;
    Button btnOK ;
    Button btnCancel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remainder_tone_dialog);
        btnBrowse=(Button)findViewById(R.id.browse);
        path=(EditText)findViewById(R.id.tone_path);
        btnOK=(Button)findViewById(R.id.dialogButtonOKReminder);
        btnCancel= (Button)findViewById(R.id.dialogButtonCancelReminder);
        btnBrowse.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.browse:
                Intent intent;
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mpeg");
                startActivityForResult(Intent.createChooser(intent, "Select Via..."), 1);
                break;

            case R.id.dialogButtonOKReminder:
                break;
            case R.id.dialogButtonCancelReminder:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri audioFileUri = data.getData();
                String MP3Path = audioFileUri.getPath();
                path.setText(MP3Path);
            }
        }
    }
}
