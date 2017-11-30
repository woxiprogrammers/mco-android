package com.android.new_transaction_list;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ImagesItemNew extends RealmObject{

	@SerializedName("image_url")
	private String imageUrl;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}
}