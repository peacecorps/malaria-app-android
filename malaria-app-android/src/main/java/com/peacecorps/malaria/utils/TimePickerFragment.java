package com.peacecorps.malaria.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.peacecorps.malaria.R;

import java.util.Calendar;


/**
 * Created by Anamika Tripathi on 27/7/18.
 */
public class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;

    public void setCallBack(TimePickerDialog.OnTimeSetListener timeSetListener) {
        listener = timeSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);

        if (getActivity() != null) {
            return new TimePickerDialog(getActivity(), R.style.MyTimePicker, listener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        } else {
            ToastLogSnackBarUtil.showErrorLog("getActivity is null");
            return null;
        }
    }
}
