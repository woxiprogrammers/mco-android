package com.android.inventory_module.inventory_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class RequestComponentResponse extends RealmObject {
    @SerializedName("data")
    private RequestComponentData requestComponentData;
    @SerializedName("message")
    private String message;

    public RequestComponentData getRequestComponentData() {
        return requestComponentData;
    }

    public void setRequestComponentData(RequestComponentData requestComponentData) {
        this.requestComponentData = requestComponentData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}