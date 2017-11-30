package com.android.models.drawing;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ImagesListDrawingItem extends RealmObject{

	@SerializedName("encoded_name")
	private String imageUrl;

	@SerializedName("id")
	private int id;

	@SerializedName("title")
	private String title;

	@SerializedName("name")
	private String name;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}
}