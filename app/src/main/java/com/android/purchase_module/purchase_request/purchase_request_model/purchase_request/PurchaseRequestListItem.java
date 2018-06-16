package com.android.purchase_module.purchase_request.purchase_request_model.purchase_request;

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
    @SerializedName("have_access")
    private String haveAccess;
    @SerializedName("approved_by")
    private String approvedBy;
    @SerializedName("created_by")
    private String createdBy;

    @SerializedName("purchase_request_status")
    private String purchaseRequestStatus;

    @SerializedName("isDisproved")
    private boolean isDisproved;

    public String getPurchaseRequestStatus() {
        return purchaseRequestStatus;
    }

    public boolean isDisproved() {
        return isDisproved;
    }

    public void setDisproved(boolean disproved) {
        isDisproved = disproved;
    }

    public void setPurchaseRequestStatus(String purchaseRequestStatus) {
        this.purchaseRequestStatus = purchaseRequestStatus;
    }

    public int getId() {
        return id;
    }

    public String getHaveAccess() {
        return haveAccess;
    }

    public void setHaveAccess(String haveAccess) {
        this.haveAccess = haveAccess;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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