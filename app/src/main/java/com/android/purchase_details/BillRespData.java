package com.android.purchase_details;

import com.android.models.purchase_bill.PurchaseBillListItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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