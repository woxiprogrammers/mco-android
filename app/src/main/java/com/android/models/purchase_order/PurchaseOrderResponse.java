package com.android.models.purchase_order;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseOrderResponse extends RealmObject {
    @PrimaryKey
    private int intIndex = 0;
    @SerializedName("data")
    private PurchaseOrderRespData purchaseOrderRespData;
    @SerializedName("message")
    private String message;

    public void setPurchaseOrderRespData(PurchaseOrderRespData purchaseOrderRespData) {
        this.purchaseOrderRespData = purchaseOrderRespData;
    }

    public PurchaseOrderRespData getPurchaseOrderRespData() {
        return purchaseOrderRespData;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}