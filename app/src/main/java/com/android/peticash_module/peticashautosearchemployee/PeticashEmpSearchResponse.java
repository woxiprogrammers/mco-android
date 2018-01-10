package com.android.peticash_module.peticashautosearchemployee;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PeticashEmpSearchResponse extends RealmObject{

	@SerializedName("data")
	private RealmList<EmployeeSearchDataItem> employeesearchdata;

	@SerializedName("message")
	private String message;

	public void setEmployeesearchdata(RealmList<EmployeeSearchDataItem> employeesearchdata){
		this.employeesearchdata = employeesearchdata;
	}

	public RealmList<EmployeeSearchDataItem> getEmployeesearchdata(){
		return employeesearchdata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}