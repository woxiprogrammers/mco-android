package com.android.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EmployeeTransactionsItem extends RealmObject{


	@PrimaryKey
	@SerializedName("peticash_salary_transaction_id")
	private int id;

	@SerializedName("date")
	private String date;

	@SerializedName("amount")
	private String amount;

	@SerializedName("project_site_id")
	private int projectSiteId;

	@SerializedName("project_site_name")
	private String projectSiteName;

	@SerializedName("transaction_status_id")
	private int transactionStatusId;

	@SerializedName("transaction_status_name")
	private String transactionStatusName;

	@SerializedName("type")
	private String type;

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

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
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