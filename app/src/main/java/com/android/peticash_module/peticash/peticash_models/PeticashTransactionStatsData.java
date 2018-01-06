package com.android.peticash_module.peticash.peticash_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PeticashTransactionStatsData extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("remaining_amount")
    private float remainingAmount;
    @SerializedName("allocated_amount")
    private float allocatedAmount;
    @SerializedName("total_purchase_amount")
    private float totalPurchaseAmount;
    @SerializedName("total_advance_amount")
    private float totalAdvanceAmount;
    @SerializedName("total_salary_amount")
    private float totalSalaryAmount;

    public void setRemainingAmount(float remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public float getRemainingAmount() {
        return remainingAmount;
    }

    public void setAllocatedAmount(float allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public float getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setTotalPurchaseAmount(float totalPurchaseAmount) {
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public float getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public void setTotalAdvanceAmount(float totalAdvanceAmount) {
        this.totalAdvanceAmount = totalAdvanceAmount;
    }

    public float getTotalAdvanceAmount() {
        return totalAdvanceAmount;
    }

    public void setTotalSalaryAmount(float totalSalaryAmount) {
        this.totalSalaryAmount = totalSalaryAmount;
    }

    public float getTotalSalaryAmount() {
        return totalSalaryAmount;
    }
}