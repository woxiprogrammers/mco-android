package com.android.purchase_module.purchase_request.purchase_request_model.purchase_bill;

import com.android.utils.AppUtils;
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
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public PurchaseBillRespData getPurchaseBillRespData() {
        return purchaseBillRespData;
    }

    public void setPurchaseBillRespData(PurchaseBillRespData purchaseBillRespData) {
        this.purchaseBillRespData = purchaseBillRespData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}