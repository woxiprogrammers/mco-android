package com.android.inventory_module.assets.asset_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetMaintenanceListItem extends RealmObject{

	@SerializedName("date")
	private String date;

	@SerializedName("grn")
	private String grn;

	@SerializedName("approved_vendor_name")
	private String approvedVendorName;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("asset_maintenance_id")
	private int assetMaintenanceId;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("approved_vendor_id")
	private String approvedVendorId;

	@SerializedName("status")
	private String status;
	private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);


	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setGrn(String grn){
		this.grn = grn;
	}

	public String getGrn(){
		return grn;
	}

	public void setApprovedVendorName(String approvedVendorName){
		this.approvedVendorName = approvedVendorName;
	}

	public String getApprovedVendorName(){
		return approvedVendorName;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setAssetMaintenanceId(int assetMaintenanceId){
		this.assetMaintenanceId = assetMaintenanceId;
	}

	public int getAssetMaintenanceId(){
		return assetMaintenanceId;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setApprovedVendorId(String approvedVendorId){
		this.approvedVendorId = approvedVendorId;
	}

	public String getApprovedVendorId(){
		return approvedVendorId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}
}