package com.android.models.drawing;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class VersionsItem extends RealmObject{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}
}