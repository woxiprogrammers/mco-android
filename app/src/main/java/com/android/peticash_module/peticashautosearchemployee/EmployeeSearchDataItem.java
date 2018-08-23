package com.android.peticash_module.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EmployeeSearchDataItem extends RealmObject{

	/*
	"employee_id": 4,
			"format_employee_id": "M109",
			"employee_name": "Raju",
			"per_day_wages": 500,
			"employee_profile_picture": "/assets/global/img/logo.jpg",
			"is_transaction_pending": false,
			"balance": 0*/
	@PrimaryKey
	@SerializedName("employee_id")
	private int employeeId;

	@SerializedName("employee_name")
	private String employeeName;

	@SerializedName("format_employee_id")
	private String formatEmployeeId;

	@SerializedName("per_day_wages")
	private int perDayWages;

	@SerializedName("employee_profile_picture")
	private String employeeProfilePicture;

	@SerializedName("is_transaction_pending")
	private boolean isTransactionPending;

	@SerializedName("balance")
	private int balance;

	@SerializedName("advance_after_last_salary")
	private int advanceAmount;

	public boolean isTransactionPending() {
		return isTransactionPending;
	}

	public void setTransactionPending(boolean transactionPending) {
		isTransactionPending = transactionPending;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(int advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	public void setEmployeeId(int employeeId){
		this.employeeId = employeeId;
	}

	public int getEmployeeId(){
		return employeeId;
	}

	public void setEmployeeProfilePicture(String employeeProfilePicture){
		this.employeeProfilePicture = employeeProfilePicture;
	}

	public String getEmployeeProfilePicture(){
		return employeeProfilePicture;
	}

	public void setEmployeeName(String employeeName){
		this.employeeName = employeeName;
	}

	public String getEmployeeName(){
		return employeeName;
	}

	public void setFormatEmployeeId(String formatEmployeeId){
		this.formatEmployeeId = formatEmployeeId;
	}

	public String getFormatEmployeeId(){
		return formatEmployeeId;
	}

	public void setPerDayWages(int perDayWages){
		this.perDayWages = perDayWages;
	}

	public int getPerDayWages(){
		return perDayWages;
	}
}