package com.peacecorps.malaria;

/**
 * Created by Ankita on 8/8/2015.
 */
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TripAlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public TripAlarmService() {
        super("TripAlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Get Ready to Pack your Bags for Trip!"+"\n"+TripIndicatorFragmentActivity.mItemPicked);
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TripIndicatorFragmentActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Reminder for Trip").setSmallIcon(R.drawable.appicon_themed)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("TripAlarmService", "Notification sent.");
    }
}