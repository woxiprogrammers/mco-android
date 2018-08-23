package com.android.purchase_module.purchase_request.purchase_request_model.purchase_order;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class QuotationImagesItem extends RealmObject{

	@SerializedName("image_url")
	private String imageUrl;

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}
}