package com.example.notifiy_samplefcm;


import android.util.Log;

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
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d(TAG,"onMessageReceived 호출됨: " + "title :"+ remoteMessage.getNotification().getTitle() + ", body : "+ remoteMessage.getNotification().getBody());

    }

}
