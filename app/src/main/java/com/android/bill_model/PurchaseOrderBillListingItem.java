package com.android.bill_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseOrderBillListingItem extends RealmObject{

	@SerializedName("bill_data")
	private RealmList<BillDataItem> billData;
	private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("grn")
	private String grn;

	public void setBillData(RealmList<BillDataItem> billData){
		this.billData = billData;
	}

	public RealmList<BillDataItem> getBillData(){
		return billData;
	}

	public String getGrn() {
		return grn;
	}

	public void setGrn(String grn) {
		this.grn = grn;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public String getMaterialName(){
		return materialName;
	}
}