package com.android.models.purchase_bill;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseBillRespData extends RealmObject {
    @SerializedName("purchase_order_bill_listing")
    private RealmList<PurchaseBillListItem> purchaseBillList;

    public RealmList<PurchaseBillListItem> getPurchaseBillList() {
        return purchaseBillList;
    }

    public void setPurchaseBillList(RealmList<PurchaseBillListItem> purchaseBillList) {
        this.purchaseBillList = purchaseBillList;
    }
}