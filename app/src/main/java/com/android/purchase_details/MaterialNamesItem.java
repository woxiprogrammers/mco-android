package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MaterialNamesItem extends RealmObject{

	@SerializedName("material_component_units")
	private RealmList<MaterialUnitsItem> materialUnits;

	@SerializedName("material_component_images")
	private RealmList<MaterialImagesItem> materialImages;

	@PrimaryKey
	@SerializedName("purchase_order_component_id")
	private int id;

	@SerializedName("material_component_name")
	private String materialName;

    public int getMaterialRequestComponentId() {
        return materialRequestComponentId;
    }

    public void setMaterialRequestComponentId(int materialRequestComponentId) {
        this.materialRequestComponentId = materialRequestComponentId;
    }

    @SerializedName("material_request_component_id")
	private int materialRequestComponentId;

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