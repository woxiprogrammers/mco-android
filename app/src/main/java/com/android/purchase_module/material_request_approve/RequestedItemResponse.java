package com.android.purchase_module.material_request_approve;

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
    @SerializedName("has_approve_access")
    private boolean isApproveAccess;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RequestedItemData getRequestedItemData() {
        return requestedItemData;
    }

    public boolean isApproveAccess() {
        return isApproveAccess;
    }

    public void setApproveAccess(boolean approveAccess) {
        isApproveAccess = approveAccess;
    }

    public void setRequestedItemData(RequestedItemData requestedItemData) {
        this.requestedItemData = requestedItemData;
    }
}