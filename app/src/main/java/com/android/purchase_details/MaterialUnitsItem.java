package com.android.purchase_details;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MaterialUnitsItem extends RealmObject {

	@SerializedName("unit")
	private String unit;

	@SerializedName("unit_id")
	private int unitId;

	public void setUnit(String unit){
		this.unit = unit;
	}

	public String getUnit(){
		return unit;
	}

	public void setUnitId(int unitId){
		this.unitId = unitId;
	}

	public int getUnitId(){
		return unitId;
	}
}