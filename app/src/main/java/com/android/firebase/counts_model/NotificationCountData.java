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

    @SerializedName("purchase_order_create_count")
    private int purchaseOrderCreateCount;

    @SerializedName("purchase_order_bill_create_count")
    private int purchaseOrderBillCreateCount;

    @SerializedName("purchase_order_request_create_count")
    private int purchaseOrderRequestCreateCount;

    @SerializedName("material_site_out_transfer_create_count")
    private int materialSiteOutTransferCreateCount;

    @SerializedName("material_site_out_transfer_approve_count")
    private int materialSiteOutTransferApproveCount;

    @SerializedName("checklist_assigned_count")
    private int checklistAssignedCount;

    @SerializedName("review_checklist_count")
    private int reviewChecklistCount;

    @SerializedName("salary_request_count")
    private int salaryRequestCount;

    @SerializedName("salary_approved_count")
    private int salaryApprovedCount;

    @SerializedName("purchase_request_approved_count")
    private int purchaseRequestApprovedCount;

    public int getSalaryRequestCount() {
        return salaryRequestCount;
    }

    public void setSalaryRequestCount(int salaryRequestCount) {
        this.salaryRequestCount = salaryRequestCount;
    }

    public int getSalaryApprovedCount() {
        return salaryApprovedCount;
    }

    public void setSalaryApprovedCount(int salaryApprovedCount) {
        this.salaryApprovedCount = salaryApprovedCount;
    }

    public int getPurchaseRequestApprovedCount() {
        return purchaseRequestApprovedCount;
    }

    public void setPurchaseRequestApprovedCount(int purchaseRequestApprovedCount) {
        this.purchaseRequestApprovedCount = purchaseRequestApprovedCount;
    }

    public int getMaterialRequestDisapprovedCount() {
        return materialRequestDisapprovedCount;
    }

    public int getPurchaseOrderCreateCount() {
        return purchaseOrderCreateCount;
    }

    public void setPurchaseOrderCreateCount(int purchaseOrderCreateCount) {
        this.purchaseOrderCreateCount = purchaseOrderCreateCount;
    }

    public int getPurchaseOrderBillCreateCount() {
        return purchaseOrderBillCreateCount;
    }

    public void setPurchaseOrderBillCreateCount(int purchaseOrderBillCreateCount) {
        this.purchaseOrderBillCreateCount = purchaseOrderBillCreateCount;
    }

    public int getPurchaseOrderRequestCreateCount() {
        return purchaseOrderRequestCreateCount;
    }

    public void setPurchaseOrderRequestCreateCount(int purchaseOrderRequestCreateCount) {
        this.purchaseOrderRequestCreateCount = purchaseOrderRequestCreateCount;
    }

    public int getMaterialSiteOutTransferCreateCount() {
        return materialSiteOutTransferCreateCount;
    }

    public void setMaterialSiteOutTransferCreateCount(int materialSiteOutTransferCreateCount) {
        this.materialSiteOutTransferCreateCount = materialSiteOutTransferCreateCount;
    }

    public int getMaterialSiteOutTransferApproveCount() {
        return materialSiteOutTransferApproveCount;
    }

    public void setMaterialSiteOutTransferApproveCount(int materialSiteOutTransferApproveCount) {
        this.materialSiteOutTransferApproveCount = materialSiteOutTransferApproveCount;
    }

    public int getChecklistAssignedCount() {
        return checklistAssignedCount;
    }

    public void setChecklistAssignedCount(int checklistAssignedCount) {
        this.checklistAssignedCount = checklistAssignedCount;
    }

    public int getReviewChecklistCount() {
        return reviewChecklistCount;
    }

    public void setReviewChecklistCount(int reviewChecklistCount) {
        this.reviewChecklistCount = reviewChecklistCount;
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