package com.android.dpr_module;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DprListingData extends RealmObject{

	@SerializedName("dpr_list")
	private RealmList<DprListItem> dprList;

	public void setDprList(RealmList<DprListItem> dprList){
		this.dprList = dprList;
	}

	public RealmList<DprListItem> getDprList(){
		return dprList;
	}
}