package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class MaterialUnitsData extends RealmObject{

	@SerializedName("material_list")
	private RealmList<MaterialNamesItem> materialNames;

	public void setMaterialNames(RealmList<MaterialNamesItem> materialNames){
		this.materialNames = materialNames;
	}

	public RealmList<MaterialNamesItem> getMaterialNames(){
		return materialNames;
	}
}