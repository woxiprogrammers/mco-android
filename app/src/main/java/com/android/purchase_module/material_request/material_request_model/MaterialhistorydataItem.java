package com.android.purchase_module.material_request.material_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MaterialhistorydataItem extends RealmObject{

	@SerializedName("display_message")
	private String message;

	@PrimaryKey
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