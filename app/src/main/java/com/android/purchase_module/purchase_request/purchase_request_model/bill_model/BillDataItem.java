package com.android.purchase_module.purchase_request.purchase_request_model.bill_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class BillDataItem extends RealmObject{

	@SerializedName("date")
	private String date;

	@SerializedName("out_time")
	private String outTime;

	@SerializedName("images")
	private RealmList<ImagesItem> images;

	@SerializedName("bill_amount")
	private String billAmount;

	@SerializedName("purchase_order_id")
	private int purchaseOrderId;

	@SerializedName("vendor_name")
	private String vendorName;

	@SerializedName("remark")
	private String remark;

	@SerializedName("material_quantity")
	private String materialQuantity;

	@SerializedName("purchase_bill_grn")
	private String purchaseBillGrn;

	@SerializedName("unit_name")
	private String unitName;

	@SerializedName("purchase_request_id")
	private int purchaseRequestId;

	@SerializedName("bill_number")
	private String billNumber;

	@SerializedName("in_time")
	private String inTime;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("purchase_request_format_id")
	private String purchaseRequestFormatId;

	@SerializedName("purchase_order_bill_id")
	private int purchaseOrderBillId;

	@SerializedName("id")
	private int id;

	@SerializedName("purchase_order_component_id")
	private int purchaseOrderComponentId;

	@SerializedName("purchase_order_format_id")
	private String purchaseOrderFormatId;

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("unit_id")
	private int unitId;

	@SerializedName("status")
	private String status;

	private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setOutTime(String outTime){
		this.outTime = outTime;
	}

	public String getOutTime(){
		return outTime;
	}

	public void setImages(RealmList<ImagesItem> images){
		this.images = images;
	}

	public RealmList<ImagesItem> getImages(){
		return images;
	}

	public void setBillAmount(String billAmount){
		this.billAmount = billAmount;
	}

	public String getBillAmount(){
		return billAmount;
	}

	public void setPurchaseOrderId(int purchaseOrderId){
		this.purchaseOrderId = purchaseOrderId;
	}

	public int getPurchaseOrderId(){
		return purchaseOrderId;
	}

	public void setVendorName(String vendorName){
		this.vendorName = vendorName;
	}

	public String getVendorName(){
		return vendorName;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setMaterialQuantity(String materialQuantity){
		this.materialQuantity = materialQuantity;
	}

	public String getMaterialQuantity(){
		return materialQuantity;
	}

	public void setPurchaseBillGrn(String purchaseBillGrn){
		this.purchaseBillGrn = purchaseBillGrn;
	}

	public String getPurchaseBillGrn(){
		return purchaseBillGrn;
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

	public void setBillNumber(String billNumber){
		this.billNumber = billNumber;
	}

	public String getBillNumber(){
		return billNumber;
	}

	public void setInTime(String inTime){
		this.inTime = inTime;
	}

	public String getInTime(){
		return inTime;
	}

	public void setVehicleNumber(String vehicleNumber){
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public void setPurchaseRequestFormatId(String purchaseRequestFormatId){
		this.purchaseRequestFormatId = purchaseRequestFormatId;
	}

	public String getPurchaseRequestFormatId(){
		return purchaseRequestFormatId;
	}

	public void setPurchaseOrderBillId(int purchaseOrderBillId){
		this.purchaseOrderBillId = purchaseOrderBillId;
	}

	public int getPurchaseOrderBillId(){
		return purchaseOrderBillId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPurchaseOrderComponentId(int purchaseOrderComponentId){
		this.purchaseOrderComponentId = purchaseOrderComponentId;
	}

	public int getPurchaseOrderComponentId(){
		return purchaseOrderComponentId;
	}

	public void setPurchaseOrderFormatId(String purchaseOrderFormatId){
		this.purchaseOrderFormatId = purchaseOrderFormatId;
	}

	public String getPurchaseOrderFormatId(){
		return purchaseOrderFormatId;
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

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}