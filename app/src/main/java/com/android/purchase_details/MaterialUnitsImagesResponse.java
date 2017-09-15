package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialUnitsImagesResponse extends RealmObject{

	@SerializedName("material_units_data")
	private MaterialUnitsData materialUnitsData;

	@SerializedName("message")
	private String message;

	public void setMaterialUnitsData(MaterialUnitsData materialUnitsData){
		this.materialUnitsData = materialUnitsData;
	}

	public MaterialUnitsData getMaterialUnitsData(){
		return materialUnitsData;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}