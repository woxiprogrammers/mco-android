package com.android.purchase_module.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseOrderRequestdata extends RealmObject{

	@SerializedName("purchase_order_request_list")
	private RealmList<PurchaseOrderRequestListItem> purchaseOrderRequestlist;

	public void setPurchaseOrderRequestlist(RealmList<PurchaseOrderRequestListItem> purchaseOrderRequestlist){
		this.purchaseOrderRequestlist = purchaseOrderRequestlist;
	}

	public RealmList<PurchaseOrderRequestListItem> getPurchaseOrderRequestlist(){
		return purchaseOrderRequestlist;
	}
}