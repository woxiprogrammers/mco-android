package com.android.purchase_details;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillRespData {
    @SerializedName("purchase_bill_list")
    private List<PurchaseBillListItem> purchaseBillList;

    public List<PurchaseBillListItem> getPurchaseBillList() {
        return purchaseBillList;
    }

    public void setPurchaseBillList(List<PurchaseBillListItem> purchaseBillList) {
        this.purchaseBillList = purchaseBillList;
    }
}