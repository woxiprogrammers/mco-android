package com.android.dummy;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DummyCheckdata extends RealmObject{

	@SerializedName("purchase_order_bill_listing")
	private RealmList<PurchaseOrderBillListingItem> purchaseOrderBillListing;

	public void setPurchaseOrderBillListing(RealmList<PurchaseOrderBillListingItem> purchaseOrderBillListing){
		this.purchaseOrderBillListing = purchaseOrderBillListing;
	}

	public RealmList<PurchaseOrderBillListingItem> getPurchaseOrderBillListing(){
		return purchaseOrderBillListing;
	}
}