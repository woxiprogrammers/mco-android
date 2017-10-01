package com.android.dummy;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RequestedItemResponse extends RealmObject {
    @PrimaryKey
    private int index = 0;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private RequestedItemData requestedItemData;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setRequestedItemData(RequestedItemData requestedItemData) {
        this.requestedItemData = requestedItemData;
    }

    public RequestedItemData getRequestedItemData() {
        return requestedItemData;
    }
}