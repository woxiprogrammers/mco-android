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
}