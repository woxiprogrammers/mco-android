package com.android.purchase_module.bill_model;


import com.android.purchase_module.material_request_approve.UnitQuantityItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class UnitsResponseData extends RealmObject {

	@SerializedName("allowed_quantity_unit")
	private RealmList<UnitQuantityItem> allowedQuantityUnit;

	public void setAllowedQuantityUnit(RealmList<UnitQuantityItem> allowedQuantityUnit){
		this.allowedQuantityUnit = allowedQuantityUnit;
	}

	public RealmList<UnitQuantityItem> getAllowedQuantityUnit(){
		return allowedQuantityUnit;
	}
}