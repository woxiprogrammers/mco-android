package com.android.models.awarenessmodels;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class FileDetailsItem extends RealmObject{

	@SerializedName("extension")
	private String extension;

	@SerializedName("name")
	private String name;

	public void setExtension(String extension){
		this.extension = extension;
	}

	public String getExtension(){
		return extension;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}