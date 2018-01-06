package com.android.inventory_module.inventory_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RequestComponentData extends RealmObject{

	@SerializedName("request_component_listing")
	private RealmList<RequestComponentListingItem> requestComponentListing;

	public void setRequestComponentListing(RealmList<RequestComponentListingItem> requestComponentListing){
		this.requestComponentListing = requestComponentListing;
	}

	public RealmList<RequestComponentListingItem> getRequestComponentListing(){
		return requestComponentListing;
	}
}