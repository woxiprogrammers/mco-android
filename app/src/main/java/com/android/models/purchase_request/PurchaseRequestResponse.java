package com.android.models.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseRequestResponse extends RealmObject {
    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("message")
    private String message;
    @SerializedName("purchaseRequestRespData")
    private PurchaseRequestRespData purchaseRequestRespData;

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setPurchaseRequestRespData(PurchaseRequestRespData purchaseRequestRespData) {
        this.purchaseRequestRespData = purchaseRequestRespData;
    }

    public PurchaseRequestRespData getPurchaseRequestRespData() {
        return purchaseRequestRespData;
    }
}