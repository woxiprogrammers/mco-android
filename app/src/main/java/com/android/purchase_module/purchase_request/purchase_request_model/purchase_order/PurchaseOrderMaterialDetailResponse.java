package com.android.purchase_module.purchase_request.purchase_request_model.purchase_order;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseOrderMaterialDetailResponse extends RealmObject {

	@SerializedName("data")
	private PurchaseOrderDetailData purchaseOrderDetailData;

	@SerializedName("message")
	private String message;

	public void setPurchaseOrderDetailData(PurchaseOrderDetailData purchaseOrderDetailData){
		this.purchaseOrderDetailData = purchaseOrderDetailData;
	}

	public PurchaseOrderDetailData getPurchaseOrderDetailData(){
		return purchaseOrderDetailData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}