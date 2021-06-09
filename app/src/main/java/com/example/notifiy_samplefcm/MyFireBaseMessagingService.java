package com.example.notifiy_samplefcm;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM";

    public MyFireBaseMessagingService(){

    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG,"onNewToken 호출됨: " + token);
        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"From: "+remoteMessage.getFrom());

        if(remoteMessage.getData().size() > 0){
            Log.d(TAG,"Message data payload: "+ remoteMessage.getData());

            if(true){

            }else{
                handleNow();
            }
        }

        if(remoteMessage.getNotification() != null){
            Log.d(TAG,"Message Notification Body : "+remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage.getNotification().getBody());
    }

    private void handleNow(){
        Log.d(TAG,"Short lived task is done");
    }

    private void sendRegistrationToServer(String token){

    }

    private void sendNotification(String messageBody){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this,channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(getString(R.string.fcm_message))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0,notificationBuilder.build());
    }

}
