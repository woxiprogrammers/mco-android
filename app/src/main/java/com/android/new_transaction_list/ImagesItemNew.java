package com.android.new_transaction_list;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ImagesItemNew extends RealmObject{

	@SerializedName("image_url")
	private String imageUrl;

	@SerializedName("image_status")
	private String image_status;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public String getImage_status() {
		return image_status;
	}

	public void setImage_status(String image_status) {
		this.image_status = image_status;
	}
}