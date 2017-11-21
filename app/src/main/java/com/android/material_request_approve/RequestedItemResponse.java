package com.android.material_request_approve;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RequestedItemData getRequestedItemData() {
        return requestedItemData;
    }

    public void setRequestedItemData(RequestedItemData requestedItemData) {
        this.requestedItemData = requestedItemData;
    }
}