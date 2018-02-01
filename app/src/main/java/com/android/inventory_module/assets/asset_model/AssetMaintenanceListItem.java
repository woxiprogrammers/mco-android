package com.android.inventory_module.assets.asset_model;

import com.android.purchase_module.purchase_request.purchase_request_model.purchase_details.ImageItem;
import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
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

	private boolean is_transaction_created;

	public boolean isIs_transaction_created() {
		return is_transaction_created;
	}

	public void setIs_transaction_created(boolean is_transaction_created) {
		this.is_transaction_created = is_transaction_created;
	}

	private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

	@SerializedName("images")
	private RealmList<ImageItem> imageItems;

	public RealmList<ImageItem> getImageItems() {
		return imageItems;
	}

	public void setImageItems(RealmList<ImageItem> imageItems) {
		this.imageItems = imageItems;
	}

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