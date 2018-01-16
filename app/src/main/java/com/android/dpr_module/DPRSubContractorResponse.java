package com.android.dpr_module;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DPRSubContractorResponse extends RealmObject{

	@SerializedName("data")
	private RealmList<DprdataItem> dprdata;

	@SerializedName("message")
	private String message;

	public void setDprdata(RealmList<DprdataItem> dprdata){
		this.dprdata = dprdata;
	}

	public RealmList<DprdataItem> getDprdata(){
		return dprdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}