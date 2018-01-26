package com.android.firebase.counts_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NotificationCountData extends RealmObject {
    @PrimaryKey
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("material_request_disapproved_count")
    private int materialRequestDisapprovedCount;
    @SerializedName("material_request_create_count")
    private int materialRequestCreateCount;
    @SerializedName("purchase_request_disapproved_count")
    private int purchaseRequestDisapprovedCount;
    @SerializedName("purchase_request_create_count")
    private int purchaseRequestCreateCount;

    public int getMaterialRequestDisapprovedCount() {
        return materialRequestDisapprovedCount;
    }

    public void setMaterialRequestDisapprovedCount(int materialRequestDisapprovedCount) {
        this.materialRequestDisapprovedCount = materialRequestDisapprovedCount;
    }

    public int getMaterialRequestCreateCount() {
        return materialRequestCreateCount;
    }

    public void setMaterialRequestCreateCount(int materialRequestCreateCount) {
        this.materialRequestCreateCount = materialRequestCreateCount;
    }

    public int getPurchaseRequestDisapprovedCount() {
        return purchaseRequestDisapprovedCount;
    }

    public void setPurchaseRequestDisapprovedCount(int purchaseRequestDisapprovedCount) {
        this.purchaseRequestDisapprovedCount = purchaseRequestDisapprovedCount;
    }

    public int getPurchaseRequestCreateCount() {
        return purchaseRequestCreateCount;
    }

    public void setPurchaseRequestCreateCount(int purchaseRequestCreateCount) {
        this.purchaseRequestCreateCount = purchaseRequestCreateCount;
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