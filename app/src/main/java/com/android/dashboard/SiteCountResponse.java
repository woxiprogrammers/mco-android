package com.android.dashboard;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class SiteCountResponse extends RealmObject{

	@SerializedName("message")
	private String message;

	@SerializedName("sitecountData")
	private SitecountData sitecountData;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setSitecountData(SitecountData sitecountData){
		this.sitecountData = sitecountData;
	}

	public SitecountData getSitecountData(){
		return sitecountData;
	}
}