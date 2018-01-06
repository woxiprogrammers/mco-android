package com.android.purchase_module.purchase_details;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseDetailsData extends RealmObject {
    @SerializedName("item_list")
    private RealmList<ItemListItem> itemList;

    public RealmList<ItemListItem> getItemList() {
        return itemList;
    }

    public void setItemList(RealmList<ItemListItem> itemList) {
        this.itemList = itemList;
    }
}