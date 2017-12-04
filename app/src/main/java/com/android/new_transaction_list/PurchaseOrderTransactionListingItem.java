package com.android.new_transaction_list;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseOrderTransactionListingItem extends RealmObject{

	@SerializedName("out_time")
	private String outTime;

	@SerializedName("grn")
	private String grn;

	@SerializedName("images")
	private RealmList<ImagesItemNew> images;

	@SerializedName("bill_amount")
	private String billAmount;

	@SerializedName("transaction_data")
	private RealmList<TransactionDataItem> transactionData;

	@PrimaryKey
	@SerializedName("purchase_order_transaction_id")
	private int purchaseOrderTransactionId;

	@SerializedName("purchase_order_id")
	private int purchaseOrderId;

	@SerializedName("purchase_order_transaction_status_id")
	private int purchaseOrderTransactionStatusId;

	@SerializedName("remark")
	private String remark;

	@SerializedName("purchase_order_transaction_status")
	private String purchaseOrderTransactionStatus;

	@SerializedName("bill_number")
	private String billNumber;

	@SerializedName("in_time")
	private String inTime;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("purchase_order_format_id")
	private String purchaseOrderFormatId;

	@SerializedName("material_name")
	private String materialName;

	private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

	public void setOutTime(String outTime){
		this.outTime = outTime;
	}

	public String getOutTime(){
		return outTime;
	}

	public void setGrn(String grn){
		this.grn = grn;
	}

	public String getGrn(){
		return grn;
	}

	public void setImages(RealmList<ImagesItemNew> images){
		this.images = images;
	}

	public RealmList<ImagesItemNew> getImages(){
		return images;
	}

	public void setBillAmount(String billAmount){
		this.billAmount = billAmount;
	}

	public String getBillAmount(){
		return billAmount;
	}

	public void setTransactionData(RealmList<TransactionDataItem> transactionData){
		this.transactionData = transactionData;
	}

	public RealmList<TransactionDataItem> getTransactionData(){
		return transactionData;
	}

	public void setPurchaseOrderTransactionId(int purchaseOrderTransactionId){
		this.purchaseOrderTransactionId = purchaseOrderTransactionId;
	}

	public int getPurchaseOrderTransactionId(){
		return purchaseOrderTransactionId;
	}

	public void setPurchaseOrderId(int purchaseOrderId){
		this.purchaseOrderId = purchaseOrderId;
	}

	public int getPurchaseOrderId(){
		return purchaseOrderId;
	}

	public void setPurchaseOrderTransactionStatusId(int purchaseOrderTransactionStatusId){
		this.purchaseOrderTransactionStatusId = purchaseOrderTransactionStatusId;
	}

	public int getPurchaseOrderTransactionStatusId(){
		return purchaseOrderTransactionStatusId;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setPurchaseOrderTransactionStatus(String purchaseOrderTransactionStatus){
		this.purchaseOrderTransactionStatus = purchaseOrderTransactionStatus;
	}

	public String getPurchaseOrderTransactionStatus(){
		return purchaseOrderTransactionStatus;
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
}