package com.android.peticashautosearchemployee;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EmployeesearchdataItem extends RealmObject{

	@SerializedName("extra_amount_paid")
	private int extraAmountPaid;

	@PrimaryKey
	@SerializedName("employee_id")
	private int employeeId;

	@SerializedName("employee_profile_picture")
	private String employeeProfilePicture;

	@SerializedName("total_amount_paid")
	private int totalAmountPaid;

	@SerializedName("employee_name")
	private String employeeName;

	@SerializedName("format_employee_id")
	private String formatEmployeeId;

	@SerializedName("per_day_wages")
	private int perDayWages;

	public void setExtraAmountPaid(int extraAmountPaid){
		this.extraAmountPaid = extraAmountPaid;
	}

	public int getExtraAmountPaid(){
		return extraAmountPaid;
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

	public void setTotalAmountPaid(int totalAmountPaid){
		this.totalAmountPaid = totalAmountPaid;
	}

	public int getTotalAmountPaid(){
		return totalAmountPaid;
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