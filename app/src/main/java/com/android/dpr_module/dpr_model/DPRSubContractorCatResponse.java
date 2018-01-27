package com.android.dpr_module.dpr_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DPRSubContractorCatResponse extends RealmObject{

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private RealmList<SubdataItem> subdata;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setSubdata(RealmList<SubdataItem> subdata){
		this.subdata = subdata;
	}

	public RealmList<SubdataItem> getSubdata(){
		return subdata;
	}
}