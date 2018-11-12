package com.peacecorps.malaria.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.peacecorps.malaria.R;

import java.util.Calendar;

/**
 * Created by Anamika Tripathi on 28/7/18.
 */
public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener ondateSet;

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        if (getActivity() != null) {
            return new DatePickerDialog(getActivity(), R.style.MyDatePicker, ondateSet, year, month, day);
        } else {
            ToastLogSnackBarUtil.showToast(getContext(), "Something went wrong! Please try again");
            ToastLogSnackBarUtil.showErrorLog("getActivity is null");
            return null;
        }
    }
}
