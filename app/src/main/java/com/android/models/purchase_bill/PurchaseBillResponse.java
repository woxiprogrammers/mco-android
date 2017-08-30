package com.android.models.purchase_bill;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseBillResponse extends RealmObject {
    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("purchaseBillRespData")
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