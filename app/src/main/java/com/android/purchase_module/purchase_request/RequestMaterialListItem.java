package com.android.purchase_module.purchase_request;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RequestMaterialListItem extends RealmObject{

	@PrimaryKey
	@SerializedName("material_request_component_id")
	private int id;

	@SerializedName("name")
	private String materialName;

	@SerializedName("vendor_relations")
	private RealmList<VendorsItem> vendors;

	@SerializedName("quantity")
	private String quantity;

	public boolean isIs_approved() {
		return is_approved;
	}

	public void setIs_approved(boolean is_approved) {
		this.is_approved = is_approved;
	}

	@SerializedName("is_approved")
	private boolean is_approved;

	@SerializedName("unit")
	private String unitName;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public void setMaterialName(String materialName){
		this.materialName = materialName;
	}

	public String getMaterialName(){
		return materialName;
	}

	public void setVendors(RealmList<VendorsItem> vendors){
		this.vendors = vendors;
	}

	public RealmList<VendorsItem> getVendors(){
		return vendors;
	}
}