package com.android.dummy;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ImagesItem extends RealmObject{

	@SerializedName("image_url")
	private String imageUrl;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}
}