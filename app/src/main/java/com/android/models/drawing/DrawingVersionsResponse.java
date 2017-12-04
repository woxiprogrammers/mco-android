package com.android.models.drawing;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class DrawingVersionsResponse extends RealmObject{

	@SerializedName("data")
	private Versionsdata versionsdata;

	@SerializedName("message")
	private String message;

	public void setVersionsdata(Versionsdata versionsdata){
		this.versionsdata = versionsdata;
	}

	public Versionsdata getVersionsdata(){
		return versionsdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}