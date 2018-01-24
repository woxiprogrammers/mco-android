package com.android.purchase_module.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class VendorsItem extends RealmObject{

	@SerializedName("rate")
	private String rate;

	@SerializedName("vendor_id")
	private int vendorId;

	@SerializedName("vendor_name")
	private String vendorName;

	@SerializedName("rate_per_tax")
	private String ratePerTax;

	@SerializedName("total_rate_per_tax")
	private String totalRatePerTax;

	public void setRate(String rate){
		this.rate = rate;
	}

	public String getRate(){
		return rate;
	}

	public void setVendorId(int vendorId){
		this.vendorId = vendorId;
	}

	public int getVendorId(){
		return vendorId;
	}

	public void setVendorName(String vendorName){
		this.vendorName = vendorName;
	}

	public String getVendorName(){
		return vendorName;
	}

	public void setRatePerTax(String ratePerTax){
		this.ratePerTax = ratePerTax;
	}

	public String getRatePerTax(){
		return ratePerTax;
	}

	public void setTotalRatePerTax(String totalRatePerTax){
		this.totalRatePerTax = totalRatePerTax;
	}

	public String getTotalRatePerTax(){
		return totalRatePerTax;
	}
}