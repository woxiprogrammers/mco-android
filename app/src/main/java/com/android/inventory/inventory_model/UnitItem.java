package com.android.inventory.inventory_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class UnitItem extends RealmObject{

	@SerializedName("unit_name")
	private String unitName;

	@SerializedName("unit_id")
	private int unitId;

	public void setUnitName(String unitName){
		this.unitName = unitName;
	}

	public String getUnitName(){
		return unitName;
	}

	public void setUnitId(int unitId){
		this.unitId = unitId;
	}

	public int getUnitId(){
		return unitId;
	}
}