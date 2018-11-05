package com.android.purchase_module.purchase_request.purchase_request_model.bill_model;


import com.android.purchase_module.material_request.material_request_model.UnitQuantityItem;
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