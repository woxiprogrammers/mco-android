package com.android.models.drawing;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class CommentsListItem extends RealmObject{

	@SerializedName("comment")
	private String name;

	@SerializedName("id")
	private int id;

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
}