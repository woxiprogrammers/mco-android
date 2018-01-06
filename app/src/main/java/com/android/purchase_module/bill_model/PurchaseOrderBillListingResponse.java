package com.android.purchase_module.bill_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseOrderBillListingResponse extends RealmObject{

	@SerializedName("page_id")
	private String pageId;

	@SerializedName("data")
	private PurchaseOrderBillListingData dummyCheckdata;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	public void setPageId(String pageId){
		this.pageId = pageId;
	}

	public String getPageId(){
		return pageId;
	}

	public void setDummyCheckdata(PurchaseOrderBillListingData dummyCheckdata){
		this.dummyCheckdata = dummyCheckdata;
	}

	public PurchaseOrderBillListingData getDummyCheckdata(){
		return dummyCheckdata;
	}

	public void setNextUrl(String nextUrl){
		this.nextUrl = nextUrl;
	}

	public String getNextUrl(){
		return nextUrl;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}