package com.android.peticash_module.peticashautosearchemployee;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TransactionsData extends RealmObject{

	@SerializedName("employee_id")
	private int employeeId;

	@SerializedName("employee_name")
	private String employeeName;

	@SerializedName("employee_transactions")
	private RealmList<EmployeeTransactionsItem> employeeTransactions;

	@SerializedName("format_employee_id")
	private String formatEmployeeId;

	public void setEmployeeId(int employeeId){
		this.employeeId = employeeId;
	}

	public int getEmployeeId(){
		return employeeId;
	}

	public void setEmployeeName(String employeeName){
		this.employeeName = employeeName;
	}

	public String getEmployeeName(){
		return employeeName;
	}

	public void setEmployeeTransactions(RealmList<EmployeeTransactionsItem> employeeTransactions){
		this.employeeTransactions = employeeTransactions;
	}

	public RealmList<EmployeeTransactionsItem> getEmployeeTransactions(){
		return employeeTransactions;
	}

	public void setFormatEmployeeId(String formatEmployeeId){
		this.formatEmployeeId = formatEmployeeId;
	}

	public String getFormatEmployeeId(){
		return formatEmployeeId;
	}
}