package com.android.models.awarenessmodels;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FileDetailsItem extends RealmObject{

	@SerializedName("extension")
	private String extension;

	@SerializedName("name")
	private String name;

	@PrimaryKey
	@SerializedName("id")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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