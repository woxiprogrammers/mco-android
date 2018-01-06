package com.android.purchase_module.bill_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UnitsResponse extends RealmObject{

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private UnitsResponseData unitsResponseData;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUnitsResponseData(UnitsResponseData unitsResponseData){
		this.unitsResponseData = unitsResponseData;
	}

	public UnitsResponseData getUnitsResponseData(){
		return unitsResponseData;
	}
}