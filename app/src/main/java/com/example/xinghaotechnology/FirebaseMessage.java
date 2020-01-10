package com.example.xinghaotechnology;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("token", s);
        SharedPreferences.Editor editor = getSharedPreferences("FIREBASE", MODE_PRIVATE).edit();
        editor.putString("token", s);
        editor.apply();
    }
}
