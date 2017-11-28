package com.android.models.drawing;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ImagesListDrawingItem extends RealmObject{

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("id")
	private int id;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}