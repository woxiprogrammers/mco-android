package com.android.purchase_module.purchase_details;

import com.android.purchase_module.purchase_bill.PurchaseBillListItem;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class BillRespData extends RealmObject {
    @SerializedName("purchase_bill_list")
    private RealmList<PurchaseBillListItem> purchaseBillList;

    public RealmList<PurchaseBillListItem> getPurchaseBillList() {
        return purchaseBillList;
    }

    public void setPurchaseBillList(RealmList<PurchaseBillListItem> purchaseBillList) {
        this.purchaseBillList = purchaseBillList;
    }
}