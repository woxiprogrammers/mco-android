package com.android.checklisthome.checklist_model.checkpoints_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CheckPointsResponse extends RealmObject{

	@SerializedName("data")
	private CheckPointsdata checkPointsdata;

	@SerializedName("message")
	private String message;

	public void setCheckPointsdata(CheckPointsdata checkPointsdata){
		this.checkPointsdata = checkPointsdata;
	}

	public CheckPointsdata getCheckPointsdata(){
		return checkPointsdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}