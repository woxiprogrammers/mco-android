package com.android.inventory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class InventoryDataResponse extends RealmObject {

	@SerializedName("material_list")
	private List<MaterialListItem> materialList;

	public void setMaterialList(List<MaterialListItem> materialList){
		this.materialList = materialList;
	}

	public List<MaterialListItem> getMaterialList(){
		return materialList;
	}
}