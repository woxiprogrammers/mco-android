package com.android.dashboard.notification_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class NotificationCountSiteResponse extends RealmObject{

	@SerializedName("data")
	private NotificationCountSiteData notificationCountData;

	@SerializedName("message")
	private String message;

	public void setNotificationCountData(NotificationCountSiteData notificationCountData){
		this.notificationCountData = notificationCountData;
	}

	public NotificationCountSiteData getNotificationCountData(){
		return notificationCountData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}