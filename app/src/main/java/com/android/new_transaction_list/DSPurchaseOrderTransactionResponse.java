package com.android.new_transaction_list;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class DSPurchaseOrderTransactionResponse extends RealmObject{

	@SerializedName("page_id")
	private String pageId;

	@SerializedName("purchaseOrderTransactionDSdata")
	private PurchaseOrderTransactionDSdata purchaseOrderTransactionDSdata;

	@SerializedName("message")
	private String message;

	public void setPageId(String pageId){
		this.pageId = pageId;
	}

	public String getPageId(){
		return pageId;
	}

	public void setPurchaseOrderTransactionDSdata(PurchaseOrderTransactionDSdata purchaseOrderTransactionDSdata){
		this.purchaseOrderTransactionDSdata = purchaseOrderTransactionDSdata;
	}

	public PurchaseOrderTransactionDSdata getPurchaseOrderTransactionDSdata(){
		return purchaseOrderTransactionDSdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}