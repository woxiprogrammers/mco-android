package com.android.dpr_module;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class DprListingResponse extends RealmObject {

    @SerializedName("data")
    private DprListingData dprListingData;

    @SerializedName("message")
    private String message;

    public void setDprListingData(DprListingData dprListingData) {
        this.dprListingData = dprListingData;
    }

    public DprListingData getDprListingData() {
        return dprListingData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}