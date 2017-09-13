package com.android.purchase_details;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseDetailsData extends RealmObject{

	@SerializedName("item_list")
	private RealmList<ItemListItem> itemList;

	public void setItemList(RealmList<ItemListItem> itemList){
		this.itemList = itemList;
	}

	public RealmList<ItemListItem> getItemList(){
		return itemList;
	}
}