package com.android.models.purchase_bill;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseBillRespData extends RealmObject {
    @SerializedName("purchase_bill_list")
    private RealmList<PurchaseBillListItem> purchaseBillList;

    public void setPurchaseBillList(RealmList<PurchaseBillListItem> purchaseBillList) {
        this.purchaseBillList = purchaseBillList;
    }

    public RealmList<PurchaseBillListItem> getPurchaseBillList() {
        return purchaseBillList;
    }
}