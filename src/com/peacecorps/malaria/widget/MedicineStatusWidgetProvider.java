package com.peacecorps.malaria.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.peacecorps.malaria.R;
import com.peacecorps.malaria.fragment.ThirdAnalyticFragment;
import com.peacecorps.malaria.activities.DayFragmentActivity;
import com.peacecorps.malaria.db.DatabaseSQLiteHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yatna on 28/5/16.
 */
public class MedicineStatusWidgetProvider extends AppWidgetProvider{

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Date date= Calendar.getInstance().getTime();
            Calendar cal = Calendar.getInstance();
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            int year=cal.get(Calendar.YEAR);

            Log.d("widget", " "+day +" "+month +" "+year);
            DatabaseSQLiteHelper a;
            a=new DatabaseSQLiteHelper(context.getApplicationContext());
            String dat=a.getMedicationData(day,month,year);
           // String dat="no";
            // Create an Intent to launch DayActivity
            Intent intent = new Intent(context, DayFragmentActivity.class);
            intent.putExtra(ThirdAnalyticFragment.DATE_TAG, date.toString());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.statusappwidget);
            views.setOnClickPendingIntent(R.id.medicationstatusdisplay, pendingIntent);
            //views.setTextViewText(R.id.textView_info,preferences.getString("view_upcoming_reminder", " No upcoming reminder has been set"));

            if(dat.compareTo("yes")==0){
                views.setImageViewResource(R.id.medicationstatusdisplay,R.drawable.accept_medi_checked_);
            }
            else if(dat.compareTo("no")==0)
            {
                views.setImageViewResource(R.id.medicationstatusdisplay,R.drawable.reject_medi_checked);
            }
            else {
                views.setImageViewResource(R.id.medicationstatusdisplay, R.drawable.accept_medi_grayscale);
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
