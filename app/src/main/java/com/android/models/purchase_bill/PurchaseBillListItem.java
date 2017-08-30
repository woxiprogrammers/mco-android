package com.android.models.purchase_bill;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseBillListItem extends RealmObject{

	@SerializedName("date")
	private String date;

	@SerializedName("purchase_request_id")
	private String purchaseRequestId;

	@SerializedName("vendor")
	private String vendor;

	@SerializedName("purchase_order_id")
	private String purchaseOrderId;

	@SerializedName("id")
	private int id;

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("material_unit")
	private String materialUnit;

	@SerializedName("purchase_bill_grn")
	private String purchaseBillGrn;

	@SerializedName("material_quantity")
	private String materialQuantity;

	@SerializedName("status")
	private String status;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setPurchaseRequestId(String purchaseRequestId){
		this.purchaseRequestId = purchaseRequestId;
	}

	public String getPurchaseRequestId(){
		return purchaseRequestId;
	}

	public void setVendor(String vendor){
		this.vendor = vendor;
	}

	public String getVendor(){
		return vendor;
	}

	public void setPurchaseOrderId(String purchaseOrderId){
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseOrderId(){
		return purchaseOrderId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setMaterialUnit(String materialUnit){
		this.materialUnit = materialUnit;
	}

	public String getMaterialUnit(){
		return materialUnit;
	}

	public void setPurchaseBillGrn(String purchaseBillGrn){
		this.purchaseBillGrn = purchaseBillGrn;
	}

	public String getPurchaseBillGrn(){
		return purchaseBillGrn;
	}

	public void setMaterialQuantity(String materialQuantity){
		this.materialQuantity = materialQuantity;
	}

	public String getMaterialQuantity(){
		return materialQuantity;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}