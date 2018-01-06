package com.android.inventory.inventory_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class InventoryAutoSuggestResponse extends RealmObject{

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private RealmList<AutoSuggestdataItem> autoSuggestdata;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setAutoSuggestdata(RealmList<AutoSuggestdataItem> autoSuggestdata){
		this.autoSuggestdata = autoSuggestdata;
	}

	public RealmList<AutoSuggestdataItem> getAutoSuggestdata(){
		return autoSuggestdata;
	}
}