package com.android.models.purchase_bill;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseBillListItem extends RealmObject{


	@SerializedName("date")
	private String date;

	@SerializedName("out_time")
	private String outTime;

	@SerializedName("challan_number")
	private String challanNumber;

	@SerializedName("bill_amount")
	private String billAmount;

	@SerializedName("purchase_order_id")
	private String purchaseOrderId;

	@PrimaryKey
	@SerializedName("purchase_bill_grn")
	private String purchaseBillGrn;

	@SerializedName("material_quantity")
	private String materialQuantity;

	@SerializedName("purchase_request_id")
	private String purchaseRequestId;

	@SerializedName("in_time")
	private String inTime;

	@SerializedName("vendor")
	private String vendor;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("material_unit")
	private String materialUnit;

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

	public String getChallanNumber() {
		return challanNumber;
	}

	public void setChallanNumber(String challanNumber) {
		this.challanNumber = challanNumber;
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

	public void setBillAmount(int billAmount) {
		billAmount = billAmount;
	}
}