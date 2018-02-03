package com.android.purchase_module.purchase_request.purchase_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseOrderRequestResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("data")
    private PurchaseOrderRequestdata purchaseOrderRequestdata;
    @SerializedName("message")
    private String message;

    public void setPurchaseOrderRequestdata(PurchaseOrderRequestdata purchaseOrderRequestdata) {
        this.purchaseOrderRequestdata = purchaseOrderRequestdata;
    }

    public PurchaseOrderRequestdata getPurchaseOrderRequestdata() {
        return purchaseOrderRequestdata;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}