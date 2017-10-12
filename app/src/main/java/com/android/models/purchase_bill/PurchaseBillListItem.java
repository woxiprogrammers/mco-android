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

	@SerializedName("purchase_order_bill_id")
	private int purchaseOrderBillId;

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

	@SerializedName("bill_number")
	private String billNumber;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@PrimaryKey
	@SerializedName("purchase_bill_grn")
	private String purchaseBillGrn;

	@SerializedName("in_time")
	private String inTime;

	@SerializedName("out_time")
	private String outTime;

	@SerializedName("bill_amount")
	private String billAmount;


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

	public int getPurchaseOrderBillId() {
		return purchaseOrderBillId;
	}

	public void setPurchaseOrderBillId(int purchaseOrderBillId) {
		this.purchaseOrderBillId = purchaseOrderBillId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(String billAmount) {
		this.billAmount = billAmount;
	}

}