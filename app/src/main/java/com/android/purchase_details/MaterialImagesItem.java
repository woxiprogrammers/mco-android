package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialImagesItem extends RealmObject{

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("image_id")
	private int imageId;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setImageId(int imageId){
		this.imageId = imageId;
	}

	public int getImageId(){
		return imageId;
	}
}