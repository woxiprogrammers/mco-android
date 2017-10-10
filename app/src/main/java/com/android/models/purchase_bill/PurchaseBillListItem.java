package com.android.models.purchase_bill;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseBillListItem extends RealmObject{



	@SerializedName("purchase_request_id")
	private int id;

	@SerializedName("purchase_request_format_id")
	private String purchaseRequestFormatId;

	@SerializedName("purchase_order_id")
	private String purchaseOrderId;

	@SerializedName("purchase_order_format_id")
	private String purchaseOrderFormatId;

	@PrimaryKey
	@SerializedName("purchase_bill_grn")
	private String purchaseBillGrn;

	@SerializedName("date")
	private String date;

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("material_quantity")
	private String materialQuantity;

	@SerializedName("unit_id")
	private int unitId;

	@SerializedName("unit_name")
	private String materialUnit;

	@SerializedName("vendor_name")
	private String vendor;

	@SerializedName("status")
	private String status;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
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

	public void setBillAmount(int billAmount) {
		billAmount = billAmount;
	}

	public String getPurchaseRequestFormatId() {
		return purchaseRequestFormatId;
	}

	public void setPurchaseRequestFormatId(String purchaseRequestFormatId) {
		this.purchaseRequestFormatId = purchaseRequestFormatId;
	}

	public String getPurchaseOrderFormatId() {
		return purchaseOrderFormatId;
	}

	public void setPurchaseOrderFormatId(String purchaseOrderFormatId) {
		this.purchaseOrderFormatId = purchaseOrderFormatId;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
}