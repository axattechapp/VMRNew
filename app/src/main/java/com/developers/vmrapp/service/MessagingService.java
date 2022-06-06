package com.developers.vmrapp.service;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.developers.vmrapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("firebasetoken", "firebase token::-" + s);
        SharedPreferences.Editor editor = getSharedPreferences("fcm_token", MODE_PRIVATE).edit();
        editor.putString("fcm_token", s);
        editor.apply();
    }



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("remotemeassage", "11" + remoteMessage.getData() + "11" + " ");


        remoteMessage.getData();
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG123", "Message data payload: " + remoteMessage.getData() + " " + remoteMessage.getData().get("started") + " " + remoteMessage.getNotification().getBody() + " " + remoteMessage.getNotification().getTitle());
//            String title = remoteMessage.getData().get("title").toString();


            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {

                Log.d("TAG", "key, " + entry.getKey() + " value " + entry.getValue());
            }
        }


        Map<String, String> data = remoteMessage.getData();
        JSONObject object = new JSONObject(data);
        Log.d("JSON_OBJECT", object.toString() + "  title:" + data.toString());
        PendingIntent pendingIntent = null;

        SharedPreferences pref = getSharedPreferences("bts_token", Context.MODE_PRIVATE);

        int userid = Integer.parseInt(pref.getString("user_id", ""));



            if (!isAppIsInBackground(getApplicationContext())) {
                Log.d("remotemeassage", "12" + remoteMessage.getNotification().getTitle() + "12");
                String channelId = "mynotification";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_text_logo)
                        .setShowWhen(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setContentTitle(remoteMessage.getNotification().getTitle())
//                        .setOngoing(true)
                        .addAction(new NotificationCompat.Action(R.drawable.ic_text_logo, "", pendingIntent))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true);
                // .setContentIntent(pendingIntent);


                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.setBigContentTitle(remoteMessage.getNotification().getTitle());
                bigTextStyle.bigText(remoteMessage.getNotification().getBody());

                builder.setStyle(bigTextStyle);


                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH);
                    channel.setShowBadge(true);
                    manager.createNotificationChannel(channel);
                }
                manager.notify(getID(), builder.build());
            }
        }



    private final static AtomicInteger c = new AtomicInteger(0);

    public static int getID() {
        return c.incrementAndGet();
    }


    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }

        return isInBackground;
    }
}
