package com.android.peticash_module.peticash.peticash_models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PeticashTransactionStatsResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("data")
    private PeticashTransactionStatsData peticashTransactionStatsData;
    @SerializedName("message")
    private String message;

    public void setPeticashTransactionStatsData(PeticashTransactionStatsData peticashTransactionStatsData) {
        this.peticashTransactionStatsData = peticashTransactionStatsData;
    }

    public PeticashTransactionStatsData getPeticashTransactionStatsData() {
        return peticashTransactionStatsData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}