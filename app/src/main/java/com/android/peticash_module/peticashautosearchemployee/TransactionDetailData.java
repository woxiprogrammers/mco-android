package com.android.peticash_module.peticashautosearchemployee;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TransactionDetailData extends RealmObject{

	@SerializedName("date")
	private String date;

	@SerializedName("out_time")
	private String outTime;

	@SerializedName("grn")
	private String grn;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("bill_amount")
	private String billAmount;

	@SerializedName("peticash_transaction_type")
	private String peticashTransactionType;

	@SerializedName("project_site_name")
	private String projectSiteName;

	@SerializedName("admin_remark")
	private String adminRemark;

	@SerializedName("remark")
	private String remark;

	@SerializedName("reference_number")
	private String referenceNumber;

	@SerializedName("unit_name")
	private String unitName;

	@SerializedName("component_type")
	private String componentType;

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("bill_number")
	private String billNumber;

	@SerializedName("in_time")
	private String inTime;

	@SerializedName("list_of_images")
	private RealmList<ListOfImagesItem> listOfImages;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("peticash_status_name")
	private String peticashStatusName;

	@SerializedName("peticash_transaction_id")
	private int peticashTransactionId;

	@SerializedName("name")
	private String name;

	@SerializedName("source_name")
	private String sourceName;

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

	public void setGrn(String grn){
		this.grn = grn;
	}

	public String getGrn(){
		return grn;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setBillAmount(String billAmount){
		this.billAmount = billAmount;
	}

	public String getBillAmount(){
		return billAmount;
	}

	public void setPeticashTransactionType(String peticashTransactionType){
		this.peticashTransactionType = peticashTransactionType;
	}

	public String getPeticashTransactionType(){
		return peticashTransactionType;
	}

	public void setProjectSiteName(String projectSiteName){
		this.projectSiteName = projectSiteName;
	}

	public String getProjectSiteName(){
		return projectSiteName;
	}

	public void setAdminRemark(String adminRemark){
		this.adminRemark = adminRemark;
	}

	public String getAdminRemark(){
		return adminRemark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setReferenceNumber(String referenceNumber){
		this.referenceNumber = referenceNumber;
	}

	public String getReferenceNumber(){
		return referenceNumber;
	}

	public void setUnitName(String unitName){
		this.unitName = unitName;
	}

	public String getUnitName(){
		return unitName;
	}

	public void setComponentType(String componentType){
		this.componentType = componentType;
	}

	public String getComponentType(){
		return componentType;
	}

	public void setPaymentType(String paymentType){
		this.paymentType = paymentType;
	}

	public String getPaymentType(){
		return paymentType;
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

	public void setListOfImages(RealmList<ListOfImagesItem> listOfImages){
		this.listOfImages = listOfImages;
	}

	public RealmList<ListOfImagesItem> getListOfImages(){
		return listOfImages;
	}

	public void setVehicleNumber(String vehicleNumber){
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public void setPeticashStatusName(String peticashStatusName){
		this.peticashStatusName = peticashStatusName;
	}

	public String getPeticashStatusName(){
		return peticashStatusName;
	}

	public void setPeticashTransactionId(int peticashTransactionId){
		this.peticashTransactionId = peticashTransactionId;
	}

	public int getPeticashTransactionId(){
		return peticashTransactionId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setSourceName(String sourceName){
		this.sourceName = sourceName;
	}

	public String getSourceName(){
		return sourceName;
	}
}