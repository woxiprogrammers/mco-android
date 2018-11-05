package com.android.peticash_module.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EmployeeTransactionsItem extends RealmObject{

	/*
			"advance_amount": 1000,
			"salary_amount": 0,
			"payable_amount": 0,
*/



	@PrimaryKey
	@SerializedName("peticash_salary_transaction_id")
	private int id;

	@SerializedName("transaction_status_id")
	private int transactionStatusId;

	@SerializedName("transaction_status_name")
	private String transactionStatusName;

	@SerializedName("project_site_id")
	private int projectSiteId;

	@SerializedName("project_site_name")
	private String projectSiteName;

	@SerializedName("type")
	private String type;

	@SerializedName("date")
	private String date;

	@SerializedName("salary_amount")
	private String salaryAmount;

	public int getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(int advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	public int getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(int payableAmount) {
		this.payableAmount = payableAmount;
	}

	@SerializedName("advance_amount")
	private int advanceAmount;

	@SerializedName("payable_amount")
	private int payableAmount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setSalaryAmount(String amount){
		this.salaryAmount = amount;
	}

	public String getSalaryAmount(){
		return salaryAmount;
	}

	public void setProjectSiteId(int projectSiteId){
		this.projectSiteId = projectSiteId;
	}

	public int getProjectSiteId(){
		return projectSiteId;
	}

	public void setProjectSiteName(String projectSiteName){
		this.projectSiteName = projectSiteName;
	}

	public String getProjectSiteName(){
		return projectSiteName;
	}

	public void setTransactionStatusId(int transactionStatusId){
		this.transactionStatusId = transactionStatusId;
	}

	public int getTransactionStatusId(){
		return transactionStatusId;
	}

	public void setTransactionStatusName(String transactionStatusName){
		this.transactionStatusName = transactionStatusName;
	}

	public String getTransactionStatusName(){
		return transactionStatusName;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}
}