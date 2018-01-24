package com.android.purchase_module.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseOrderRequestdata extends RealmObject{

	@SerializedName("list")
	private RealmList<PurchaseOrderRequestlistItem> purchaseOrderRequestlist;

	public void setPurchaseOrderRequestlist(RealmList<PurchaseOrderRequestlistItem> purchaseOrderRequestlist){
		this.purchaseOrderRequestlist = purchaseOrderRequestlist;
	}

	public RealmList<PurchaseOrderRequestlistItem> getPurchaseOrderRequestlist(){
		return purchaseOrderRequestlist;
	}
}