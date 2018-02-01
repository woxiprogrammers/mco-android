package com.android.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.android.constro360.R;
import com.android.dashboard.DashBoardActivity;
import com.android.utils.AppConstants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private NotificationChannel notificationChannel;

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("From: " + remoteMessage.getFrom());
        if (remoteMessage.getData() != null) {
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String, String> stringMap = remoteMessage.getData();
        String strBody = "";
        if (stringMap.containsKey("body")) {
            strBody = stringMap.get("body");
        }
        String strTag = "";
        if (stringMap.containsKey("tag")) {
            strTag = stringMap.get("tag");
        }
        String strTitle = "";
        if (stringMap.containsKey("title")) {
            strTitle = stringMap.get("title");
        }
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, AppConstants.FCM_NOTIFICATION_ACTIVITY_REQUEST_CODE /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null) {
                notificationChannel = new NotificationChannel(AppConstants.FCM_CHANNEL_ID,
                        AppConstants.FCM_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                notificationChannel.setLightColor(Color.WHITE);
                notificationChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(this, AppConstants.FCM_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(strTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(strBody))
                .setContentText(strBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                /*.setGroup(strTag)
                .setGroupSummary(true)*/
                .setContentIntent(pendingIntent);
        if (notificationManager != null && notificationBuilder != null) {
            Random random = new Random();
            int randomNotificationId = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(randomNotificationId, notificationBuilder.build());
        }
    }
}
