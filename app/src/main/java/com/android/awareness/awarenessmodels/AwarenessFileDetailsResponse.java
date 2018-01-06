package com.android.awareness.awarenessmodels;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AwarenessFileDetailsResponse extends RealmObject{

	@SerializedName("data")
	private AwarenesListData awarenesListData;

	@SerializedName("message")
	private String message;

	public void setAwarenesListData(AwarenesListData awarenesListData){
		this.awarenesListData = awarenesListData;
	}

	public AwarenesListData getAwarenesListData(){
		return awarenesListData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}