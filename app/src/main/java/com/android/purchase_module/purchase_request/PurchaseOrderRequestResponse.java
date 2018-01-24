package com.android.purchase_module.purchase_request;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseOrderRequestResponse extends RealmObject{

	@SerializedName("data")
	private PurchaseOrderRequestdata purchaseOrderRequestdata;

	@SerializedName("message")
	private String message;

	public void setPurchaseOrderRequestdata(PurchaseOrderRequestdata purchaseOrderRequestdata){
		this.purchaseOrderRequestdata = purchaseOrderRequestdata;
	}

	public PurchaseOrderRequestdata getPurchaseOrderRequestdata(){
		return purchaseOrderRequestdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}