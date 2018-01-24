package com.android.purchase_module.purchase_request;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RequestMaterialListItem extends RealmObject{

	@SerializedName("id")
	private int id;

	@SerializedName("material_name")
	private String materialName;

	@SerializedName("vendors")
	private RealmList<VendorsItem> vendors;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
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