package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseDetailsResponse extends RealmObject{

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("purchase_details_data")
	private PurchaseDetailsData purchaseDetailsData;

	@SerializedName("message")
	private String message;

	public void setNextUrl(String nextUrl){
		this.nextUrl = nextUrl;
	}

	public String getNextUrl(){
		return nextUrl;
	}

	public void setPurchaseDetailsData(PurchaseDetailsData purchaseDetailsData){
		this.purchaseDetailsData = purchaseDetailsData;
	}

	public PurchaseDetailsData getPurchaseDetailsData(){
		return purchaseDetailsData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}