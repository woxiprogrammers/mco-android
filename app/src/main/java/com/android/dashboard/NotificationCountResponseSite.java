package com.android.dashboard;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class NotificationCountResponseSite extends RealmObject{

	@SerializedName("data")
	private NotificationCountDataSite notificationCountData;

	@SerializedName("message")
	private String message;

	public void setNotificationCountData(NotificationCountDataSite notificationCountData){
		this.notificationCountData = notificationCountData;
	}

	public NotificationCountDataSite getNotificationCountData(){
		return notificationCountData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}