package com.android.peticash_module.peticash.peticash_models;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PeticashTransactionStatsData extends RealmObject {
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("remaining_amount")
    private String remainingAmount;
    @SerializedName("allocated_amount")
    private String allocatedAmount;
    @SerializedName("total_purchase_amount")
    private String totalPurchaseAmount;
    @SerializedName("total_advance_amount")
    private String totalAdvanceAmount;
    @SerializedName("total_salary_amount")
    private String totalSalaryAmount;
    @SerializedName("total_subcontractor_amount")
    private String totalSubcontractorAmount;

    public String getTotalSubcontractorAmount() {
        return totalSubcontractorAmount;
    }

    public void setTotalSubcontractorAmount(String totalSubcontractorAmount) {
        this.totalSubcontractorAmount = totalSubcontractorAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setAllocatedAmount(String allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public String getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setTotalPurchaseAmount(String totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public String getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalAdvanceAmount(String totalAdvanceAmount) {
        this.totalAdvanceAmount = totalAdvanceAmount;
    }

    public String getTotalAdvanceAmount() {
        return totalAdvanceAmount;
    }

    public void setTotalSalaryAmount(String totalSalaryAmount) {
        this.totalSalaryAmount = totalSalaryAmount;
    }

    public String getTotalSalaryAmount() {
        return totalSalaryAmount;
    }
}