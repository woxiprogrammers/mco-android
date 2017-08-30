package com.android.models.purchase_order;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class PurchaseOrderListItem extends RealmObject {
    @SerializedName("date")
    private String date;
    @SerializedName("purchase_request_id")
    private String purchaseRequestId;
    @SerializedName("materials")
    private String materials;
    @SerializedName("purchase_order_id")
    private String purchaseOrderId;
    @SerializedName("project")
    private String project;
    @SerializedName("id")
    private int id;
    @SerializedName("client_name")
    private String clientName;
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

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProject() {
        return project;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}