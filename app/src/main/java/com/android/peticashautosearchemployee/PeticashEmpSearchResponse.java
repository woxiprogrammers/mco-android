package com.android.peticashautosearchemployee;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PeticashEmpSearchResponse extends RealmObject{

	@SerializedName("data")
	private RealmList<EmployeesearchdataItem> employeesearchdata;

	@SerializedName("message")
	private String message;

	@SerializedName("approved_amount")
	private String approved_amount;

	public void setEmployeesearchdata(RealmList<EmployeesearchdataItem> employeesearchdata){
		this.employeesearchdata = employeesearchdata;
	}

	public RealmList<EmployeesearchdataItem> getEmployeesearchdata(){
		return employeesearchdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public String getApproved_amount() {
		return approved_amount;
	}

	public void setApproved_amount(String approved_amount) {
		this.approved_amount = approved_amount;
	}
}