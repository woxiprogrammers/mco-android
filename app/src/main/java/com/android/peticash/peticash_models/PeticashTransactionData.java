package com.android.peticash.peticash_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PeticashTransactionData extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;

    @SerializedName("peticash_purchase_amount_limit")
    private String peticashPurchaseAmountLimit;

    @SerializedName("transactions_list")
    private RealmList<DatewiseTransactionsListItem> transactionsList;

    public void setTransactionsList(RealmList<DatewiseTransactionsListItem> transactionsList) {
        this.transactionsList = transactionsList;
    }

    public RealmList<DatewiseTransactionsListItem> getTransactionsList() {
        return transactionsList;
    }

    public String getPeticashPurchaseAmountLimit() {
        return peticashPurchaseAmountLimit;
    }

    public void setPeticashPurchaseAmountLimit(String peticashPurchaseAmountLimit) {
        this.peticashPurchaseAmountLimit = peticashPurchaseAmountLimit;
    }
}