package com.android.models.purchase_request;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseRequestListItem extends RealmObject {
    @SerializedName("date")
    private String date;
    @SerializedName("purchase_request_id")
    private String purchaseRequestId;
    @SerializedName("materials")
    private String materials;
    @SerializedName("status")
    private String status;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setPurchaseRequestId(String purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public String getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getMaterials() {
        return materials;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}