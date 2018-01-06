package com.android.purchase_module.purchase_order;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ClientApprovalImagesItem extends RealmObject {

	@SerializedName("image_url")
	private String imageUrl;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}
}