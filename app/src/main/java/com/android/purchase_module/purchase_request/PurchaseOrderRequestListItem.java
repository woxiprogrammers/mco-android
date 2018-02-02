package com.android.purchase_module.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseOrderRequestListItem extends RealmObject{
/*
	{
		"data": {
		"list": [
		{
				"purchase_request_format_id": "PR5201801231",
				"user_name": "Admin ",
				"date": "Tuesday, 23 January 2018"
		}
        ]
	},
		"message": "Success"
	}*/
	@SerializedName("date")
	private String date;

	@SerializedName("purchase_order_request_id")
	private int purchaseOrderRequestId;

	@SerializedName("purchase_request_id")
	private int purchaseRequestId;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("purchase_request_format_id")
	private String purchaseRequestFormatId;

	@SerializedName("component_names")
	private String materialName;

	@SerializedName("purchase_order_done")
	private boolean purchaseOrderDone;

	public int getPurchaseOrderRequestId() {
		return purchaseOrderRequestId;
	}

	public void setPurchaseOrderRequestId(int purchaseOrderRequestId) {
		this.purchaseOrderRequestId = purchaseOrderRequestId;
	}

	public boolean isPurchaseOrderDone() {
		return purchaseOrderDone;
	}

	public void setPurchaseOrderDone(boolean purchaseOrderDone) {
		this.purchaseOrderDone = purchaseOrderDone;
	}

	public String getPurchaseRequestFormatId() {
		return purchaseRequestFormatId;
	}

	public void setPurchaseRequestFormatId(String purchaseRequestFormatId) {
		this.purchaseRequestFormatId = purchaseRequestFormatId;
	}

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