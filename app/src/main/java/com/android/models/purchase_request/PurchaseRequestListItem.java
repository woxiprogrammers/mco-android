package com.android.models.purchase_request;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseRequestListItem extends RealmObject {
    @PrimaryKey
    @SerializedName("purchase_request_id")
    private int id;
    @SerializedName("date")
    private String date;
    @SerializedName("purchase_request_format")
    private String purchaseRequestId;
    @SerializedName("materials")
    private String materials;
    @SerializedName("component_status_name")
    private String status;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(String purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}