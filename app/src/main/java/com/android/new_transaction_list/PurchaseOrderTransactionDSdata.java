package com.android.new_transaction_list;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseOrderTransactionDSdata extends RealmObject{

	@SerializedName("purchase_order_transaction_listing")
	private RealmList<PurchaseOrderTransactionListingItem> purchaseOrderTransactionListing;

	public void setPurchaseOrderTransactionListing(RealmList<PurchaseOrderTransactionListingItem> purchaseOrderTransactionListing){
		this.purchaseOrderTransactionListing = purchaseOrderTransactionListing;
	}

	public RealmList<PurchaseOrderTransactionListingItem> getPurchaseOrderTransactionListing(){
		return purchaseOrderTransactionListing;
	}
}