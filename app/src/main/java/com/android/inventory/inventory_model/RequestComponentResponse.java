package com.android.inventory.inventory_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class RequestComponentResponse extends RealmObject{

	@SerializedName("data")
	private RequestComponentData requestComponentData;

	@SerializedName("message")
	private String message;

	public void setRequestComponentData(RequestComponentData requestComponentData){
		this.requestComponentData = requestComponentData;
	}

	public RequestComponentData getRequestComponentData(){
		return requestComponentData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}