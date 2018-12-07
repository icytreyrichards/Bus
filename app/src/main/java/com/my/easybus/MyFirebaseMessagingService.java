package com.my.easybus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
        String body=remoteMessage.getNotification().getBody();
        String title=remoteMessage.getNotification().getTitle();
        String json="";
        if(body.contains("successfully Ended"))
        {
          json=body;
        }
        createNotification(body,title,json);
    }

    private void createNotification( String messageBody,String title,String json) {
        Intent intent = new Intent( this , IndexActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(json.contains("successfully Ended"))
        {
            Intent i = new Intent( this , RateTrip.class);
            try {
                JSONObject o = new JSONObject(json);
                i.putExtra("trip",o.getString("TripID"));
                i.putExtra("date",o.getString("date"));
                this.startActivity(i);
                messageBody=o.getString("msg");
            }
            catch (Exception er)
            {

            }

        }
        intent.putExtra("cmd","0");
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
}