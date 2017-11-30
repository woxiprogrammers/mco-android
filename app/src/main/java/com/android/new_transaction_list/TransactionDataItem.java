package com.android.new_transaction_list;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class TransactionDataItem extends RealmObject{

	@SerializedName("date")
	private String date;

	@SerializedName("unit_name")
	private String unitName;

	@SerializedName("purchase_request_id")
	private int purchaseRequestId;

	@SerializedName("purchase_order_transaction_component_id")
	private int purchaseOrderTransactionComponentId;

	@SerializedName("purchase_request_format_id")
	private String purchaseRequestFormatId;

	@SerializedName("vendor_name")
	private String vendorName;

	@SerializedName("purchase_order_component_id")
	private int purchaseOrderComponentId;

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("unit_id")
	private int unitId;

	@SerializedName("material_quantity")
	private String materialQuantity;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setUnitName(String unitName){
		this.unitName = unitName;
	}

	public String getUnitName(){
		return unitName;
	}

	public void setPurchaseRequestId(int purchaseRequestId){
		this.purchaseRequestId = purchaseRequestId;
	}

	public int getPurchaseRequestId(){
		return purchaseRequestId;
	}

	public void setPurchaseOrderTransactionComponentId(int purchaseOrderTransactionComponentId){
		this.purchaseOrderTransactionComponentId = purchaseOrderTransactionComponentId;
	}

	public int getPurchaseOrderTransactionComponentId(){
		return purchaseOrderTransactionComponentId;
	}

	public void setPurchaseRequestFormatId(String purchaseRequestFormatId){
		this.purchaseRequestFormatId = purchaseRequestFormatId;
	}

	public String getPurchaseRequestFormatId(){
		return purchaseRequestFormatId;
	}

	public void setVendorName(String vendorName){
		this.vendorName = vendorName;
	}

	public String getVendorName(){
		return vendorName;
	}

	public void setPurchaseOrderComponentId(int purchaseOrderComponentId){
		this.purchaseOrderComponentId = purchaseOrderComponentId;
	}

	public int getPurchaseOrderComponentId(){
		return purchaseOrderComponentId;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setUnitId(int unitId){
		this.unitId = unitId;
	}

	public int getUnitId(){
		return unitId;
	}

	public void setMaterialQuantity(String materialQuantity){
		this.materialQuantity = materialQuantity;
	}

	public String getMaterialQuantity(){
		return materialQuantity;
	}
}