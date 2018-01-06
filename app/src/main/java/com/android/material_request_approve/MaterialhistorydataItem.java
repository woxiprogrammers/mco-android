package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialhistorydataItem extends RealmObject{

	@SerializedName("message")
	private String message;

	@SerializedName("id")
	private int statusId;

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}