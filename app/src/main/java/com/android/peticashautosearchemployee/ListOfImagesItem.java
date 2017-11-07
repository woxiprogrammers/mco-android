package com.android.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ListOfImagesItem extends RealmObject{

	@SerializedName("image_url")
	private String imageUrl;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}
}