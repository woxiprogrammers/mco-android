package com.android.purchase_module.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseOrderRequestListItem extends RealmObject{

	@SerializedName("date")
	private String date;

	@SerializedName("purchase_request_id")
	private int purchaseRequestId;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("material_name")
	private String materialName;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setPurchaseRequestId(int purchaseRequestId){
		this.purchaseRequestId = purchaseRequestId;
	}

	public int getPurchaseRequestId(){
		return purchaseRequestId;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public String getMaterialName(){
		return materialName;
	}
}