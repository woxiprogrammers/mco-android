package com.android.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class EmpSalaryTransactionDetailResponse extends RealmObject{

	@SerializedName("data")
	private EmpSalaryTransactionDetailData empSalaryTransactionDetailData;

	@SerializedName("message")
	private String message;

	public void setEmpSalaryTransactionDetailData(EmpSalaryTransactionDetailData empSalaryTransactionDetailData){
		this.empSalaryTransactionDetailData = empSalaryTransactionDetailData;
	}

	public EmpSalaryTransactionDetailData getEmpSalaryTransactionDetailData(){
		return empSalaryTransactionDetailData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}