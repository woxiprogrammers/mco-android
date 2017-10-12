package com.android.purchase_details;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseDetailsResponse extends RealmObject {
    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("purchase_details_data")
    private PurchaseDetailsData purchaseDetailsData;
    @SerializedName("message")
    private String message;

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public PurchaseDetailsData getPurchaseDetailsData() {
        return purchaseDetailsData;
    }

    public void setPurchaseDetailsData(PurchaseDetailsData purchaseDetailsData) {
        this.purchaseDetailsData = purchaseDetailsData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}