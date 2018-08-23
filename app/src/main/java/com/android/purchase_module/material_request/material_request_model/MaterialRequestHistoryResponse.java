package com.android.purchase_module.material_request.material_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MaterialRequestHistoryResponse extends RealmObject{

	@SerializedName("data")
	private RealmList<MaterialhistorydataItem> materialhistorydata;

	@SerializedName("message")
	private String message;

	public void setMaterialhistorydata(RealmList<MaterialhistorydataItem> materialhistorydata){
		this.materialhistorydata = materialhistorydata;
	}

	public RealmList<MaterialhistorydataItem> getMaterialhistorydata(){
		return materialhistorydata;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}