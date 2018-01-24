package com.android.purchase_module.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class RequestedMaterialsResponse extends RealmObject{

	@SerializedName("requestmaterialsData")
	private RequestmaterialsData requestmaterialsData;

	@SerializedName("message")
	private String message;

	public void setRequestmaterialsData(RequestmaterialsData requestmaterialsData){
		this.requestmaterialsData = requestmaterialsData;
	}

	public RequestmaterialsData getRequestmaterialsData(){
		return requestmaterialsData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}