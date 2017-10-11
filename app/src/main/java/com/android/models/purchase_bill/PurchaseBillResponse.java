package com.android.models.purchase_bill;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseBillResponse extends RealmObject {
    @PrimaryKey
    private int intIndex = 0;
    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("data")
    private PurchaseBillRespData purchaseBillRespData;
    @SerializedName("message")
    private String message;

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setPurchaseBillRespData(PurchaseBillRespData purchaseBillRespData) {
        this.purchaseBillRespData = purchaseBillRespData;
    }

    public PurchaseBillRespData getPurchaseBillRespData() {
        return purchaseBillRespData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}