package com.android.peticash_module.peticash.peticash_models;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DatewiseTransactionsListItem extends RealmObject {
    @PrimaryKey
    private int primaryKey = new Random().nextInt((999999) + 11) + new Random().nextInt((999999) + 11);
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    private int passMonth, passYear;
    @SerializedName("date")
    private String date;
    @SerializedName("transaction_list")
    private RealmList<TransactionListItem> transactionList;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setTransactionList(RealmList<TransactionListItem> transactionList) {
        this.transactionList = transactionList;
    }

    public RealmList<TransactionListItem> getTransactionList() {
        return transactionList;
    }

    public int getPassMonth() {
        return passMonth;
    }

    public void setPassMonth(int passMonth) {
        this.passMonth = passMonth;
    }

    public int getPassYear() {
        return passYear;
    }

    public void setPassYear(int passYear) {
        this.passYear = passYear;
    }
}