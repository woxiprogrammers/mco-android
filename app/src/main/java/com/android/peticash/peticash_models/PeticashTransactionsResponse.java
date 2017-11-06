package com.android.peticash.peticash_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PeticashTransactionsResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("page_id")
    private String pageId;
    @SerializedName("date")
    private String date;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private PeticashTransactionData peticashTransactionData;

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageId() {
        return pageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setPeticashTransactionData(PeticashTransactionData peticashTransactionData) {
        this.peticashTransactionData = peticashTransactionData;
    }

    public PeticashTransactionData getPeticashTransactionData() {
        return peticashTransactionData;
    }
}