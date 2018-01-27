package com.android.dpr_module.dpr_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DprListingData extends RealmObject{

	@SerializedName("sub_contractor_list")
	private RealmList<DprListItem> dprList;

	public void setDprList(RealmList<DprListItem> dprList){
		this.dprList = dprList;
	}

	public RealmList<DprListItem> getDprList(){
		return dprList;
	}
}