package com.android.dpr_module.dpr_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DprListingResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("data")
    private DprListingData dprListingData;
    @SerializedName("message")
    private String message;

    public DprListingData getDprListingData() {
        return dprListingData;
    }

    public void setDprListingData(DprListingData dprListingData) {
        this.dprListingData = dprListingData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}