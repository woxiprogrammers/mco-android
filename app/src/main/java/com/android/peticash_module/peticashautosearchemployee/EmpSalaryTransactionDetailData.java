package com.android.peticash_module.peticashautosearchemployee;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class EmpSalaryTransactionDetailData extends RealmObject{

	@SerializedName("date")
	private String date;

	@SerializedName("amount")
	private String amount;

	@SerializedName("peticash_transaction_type")
	private String peticashTransactionType;

	@SerializedName("project_site_name")
	private String projectSiteName;

	@SerializedName("admin_remark")
	private String adminRemark;

	@SerializedName("employee_name")
	private String employeeName;

	@SerializedName("remark")
	private String remark;

	@SerializedName("payment_type")
	private String paymentType;

	@SerializedName("list_of_images")
	private RealmList<ListOfImagesItem> listOfImages;

	@SerializedName("peticash_status_name")
	private String peticashStatusName;

	@SerializedName("peticash_transaction_id")
	private int peticashTransactionId;

	@SerializedName("reference_user_name")
	private String referenceUserName;

	@SerializedName("days")
	private String days;

	@SerializedName("payable_amount")
	private String payableAmount;

	@SerializedName("per_day_wages")
	private String perDayWages;

	@SerializedName("pf")
	private String pf;

	@SerializedName("pt")
	private String pt;

	@SerializedName("esic")
	private String esic;

	@SerializedName("tds")
	private String tds;

	public String getPf() {
		return pf;
	}

	public void setPf(String pf) {
		this.pf = pf;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getEsic() {
		return esic;
	}

	public void setEsic(String esic) {
		this.esic = esic;
	}

	public String getTds() {
		return tds;
	}

	public void setTds(String tds) {
		this.tds = tds;
	}

	public String getPerDayWages() {
		return perDayWages;
	}

	public void setPerDayWages(String perDayWages) {
		this.perDayWages = perDayWages;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
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

	public void setEmployeeName(String employeeName){
		this.employeeName = employeeName;
	}

	public String getEmployeeName(){
		return employeeName;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setPaymentType(String paymentType){
		this.paymentType = paymentType;
	}

	public String getPaymentType(){
		return paymentType;
	}

	public void setListOfImages(RealmList<ListOfImagesItem> listOfImages){
		this.listOfImages = listOfImages;
	}

	public RealmList<ListOfImagesItem> getListOfImages(){
		return listOfImages;
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

	public void setReferenceUserName(String referenceUserName){
		this.referenceUserName = referenceUserName;
	}

	public String getReferenceUserName(){
		return referenceUserName;
	}

	public void setDays(String days){
		this.days = days;
	}

	public String getDays(){
		return days;
	}

	public void setPayableAmount(String payableAmount){
		this.payableAmount = payableAmount;
	}

	public String getPayableAmount(){
		return payableAmount;
	}
}