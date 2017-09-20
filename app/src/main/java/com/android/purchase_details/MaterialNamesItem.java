package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MaterialNamesItem extends RealmObject{

	@SerializedName("material_units")
	private RealmList<MaterialUnitsItem> materialUnits;

	@SerializedName("material_images")
	private RealmList<MaterialImagesItem> materialImages;

	@PrimaryKey
	@SerializedName("id")
	private int id;

	@SerializedName("material_name")
	private String materialName;

	public void setMaterialUnits(RealmList<MaterialUnitsItem> materialUnits){
		this.materialUnits = materialUnits;
	}

	public RealmList<MaterialUnitsItem> getMaterialUnits(){
		return materialUnits;
	}

	public void setMaterialImages(RealmList<MaterialImagesItem> materialImages){
		this.materialImages = materialImages;
	}

	public RealmList<MaterialImagesItem> getMaterialImages(){
		return materialImages;
	}

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
}