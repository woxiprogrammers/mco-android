package com.android.peticash.peticash_models;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionListItem extends RealmObject {
    @PrimaryKey
    private int primaryKey = new Random().nextInt((999999) + 11) + new Random().nextInt((999999) + 11);
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("peticash_transaction_type")
    private String peticashTransactionType;
    @SerializedName("peticash_transaction_id")
    private int peticashTransactionId;
    @SerializedName("payment_status")
    private String paymentStatus;
    @SerializedName("name")
    private String name;
    @SerializedName("peticash_transaction_amount")
    private String peticashTransactionAmount;

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setPeticashTransactionType(String peticashTransactionType) {
        this.peticashTransactionType = peticashTransactionType;
    }

    public String getPeticashTransactionType() {
        return peticashTransactionType;
    }

    public void setPeticashTransactionId(int peticashTransactionId) {
        this.peticashTransactionId = peticashTransactionId;
    }

    public int getPeticashTransactionId() {
        return peticashTransactionId;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPeticashTransactionAmount(String peticashTransactionAmount) {
        this.peticashTransactionAmount = peticashTransactionAmount;
    }

    public String getPeticashTransactionAmount() {
        return peticashTransactionAmount;
    }
}