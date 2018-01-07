package com.android.purchase_module.purchase_request.purchase_request_model.purchase_order;

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
    @SerializedName("has_create_access")
    private boolean isCreateAccess;

    public PurchaseOrderRespData getPurchaseOrderRespData() {
        return purchaseOrderRespData;
    }

    public void setPurchaseOrderRespData(PurchaseOrderRespData purchaseOrderRespData) {
        this.purchaseOrderRespData = purchaseOrderRespData;
    }

    public boolean isCreateAccess() {
        return isCreateAccess;
    }

    public void setCreateAccess(boolean createAccess) {
        isCreateAccess = createAccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}