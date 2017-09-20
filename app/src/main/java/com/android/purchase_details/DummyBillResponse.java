package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

public class DummyBillResponse{

	@SerializedName("BillRespData")
	private BillRespData billRespData;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	public void setBillRespData(BillRespData billRespData){
		this.billRespData = billRespData;
	}

	public BillRespData getBillRespData(){
		return billRespData;
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