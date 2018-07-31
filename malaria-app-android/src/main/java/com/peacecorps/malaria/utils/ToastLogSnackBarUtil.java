package com.peacecorps.malaria.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.peacecorps.malaria.R;

/**
 * Created by Anamika Tripathi on 18/7/18.
 */
public class ToastLogSnackBarUtil {

    private static final String TAG = "myTag";

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showDebugLog(String message) {
        Log.d(TAG, message);
    }

    public static void showErrorLog(String message) {
        Log.e(TAG, message);
    }

    public static void showSnackBar(Context context, View view, String s) {
        Snackbar snack = Snackbar.make(view, s, Snackbar.LENGTH_SHORT);
        View sbview = snack.getView();
        sbview.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        TextView textView = sbview.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));
        snack.show();
    }
}
