package com.android.firebase.counts_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NotificationCountResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("data")
    private NotificationCountData notificationCountData;

    public void setNotificationCountData(NotificationCountData notificationCountData) {
        this.notificationCountData = notificationCountData;
    }

    public NotificationCountData getNotificationCountData() {
        return notificationCountData;
    }

    @Override
    public String toString() {
        return
                "NotificationCountResponse{" +
                        "notification_count_data = '" + notificationCountData + '\'' +
                        "}";
    }
}