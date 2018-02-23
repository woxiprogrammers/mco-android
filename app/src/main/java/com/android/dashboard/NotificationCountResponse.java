package com.android.dashboard;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class NotificationCountResponse extends RealmObject{

	@SerializedName("notificationCountData")
	private NotificationCountData notificationCountData;

	@SerializedName("message")
	private String message;

	public void setNotificationCountData(NotificationCountData notificationCountData){
		this.notificationCountData = notificationCountData;
	}

	public NotificationCountData getNotificationCountData(){
		return notificationCountData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}