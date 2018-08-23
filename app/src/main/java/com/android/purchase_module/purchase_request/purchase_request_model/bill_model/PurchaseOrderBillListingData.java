package com.android.purchase_module.purchase_request.purchase_request_model.bill_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseOrderBillListingData extends RealmObject{

	@SerializedName("purchase_order_bill_listing")
	private RealmList<PurchaseOrderBillListingItem> purchaseOrderBillListing;

	public void setPurchaseOrderBillListing(RealmList<PurchaseOrderBillListingItem> purchaseOrderBillListing){
		this.purchaseOrderBillListing = purchaseOrderBillListing;
	}

	public RealmList<PurchaseOrderBillListingItem> getPurchaseOrderBillListing(){
		return purchaseOrderBillListing;
	}
}