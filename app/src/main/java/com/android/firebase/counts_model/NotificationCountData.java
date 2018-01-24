package com.android.firebase.counts_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class NotificationCountData extends RealmObject {
    @SerializedName("material_request_disapproved_count")
    private int materialRequestDisapprovedCount;
    @SerializedName("material_request_create_count")
    private int materialRequestCreateCount;
    @SerializedName("purchase_request_disapproved_count")
    private int purchaseRequestDisapprovedCount;
    @SerializedName("purchase_request_create_count")
    private int purchaseRequestCreateCount;

    public void setMaterialRequestDisapprovedCount(int materialRequestDisapprovedCount) {
        this.materialRequestDisapprovedCount = materialRequestDisapprovedCount;
    }

    public int getMaterialRequestDisapprovedCount() {
        return materialRequestDisapprovedCount;
    }

    public void setMaterialRequestCreateCount(int materialRequestCreateCount) {
        this.materialRequestCreateCount = materialRequestCreateCount;
    }

    public int getMaterialRequestCreateCount() {
        return materialRequestCreateCount;
    }

    public void setPurchaseRequestDisapprovedCount(int purchaseRequestDisapprovedCount) {
        this.purchaseRequestDisapprovedCount = purchaseRequestDisapprovedCount;
    }

    public int getPurchaseRequestDisapprovedCount() {
        return purchaseRequestDisapprovedCount;
    }

    public void setPurchaseRequestCreateCount(int purchaseRequestCreateCount) {
        this.purchaseRequestCreateCount = purchaseRequestCreateCount;
    }

    public int getPurchaseRequestCreateCount() {
        return purchaseRequestCreateCount;
    }

    @Override
    public String toString() {
        return
                "NotificationCountData{" +
                        "material_request_disapproved_count = '" + materialRequestDisapprovedCount + '\'' +
                        ",material_request_create_count = '" + materialRequestCreateCount + '\'' +
                        ",purchase_request_disapproved_count = '" + purchaseRequestDisapprovedCount + '\'' +
                        ",purchase_request_create_count = '" + purchaseRequestCreateCount + '\'' +
                        "}";
    }
}