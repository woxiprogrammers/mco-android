package com.android.purchase_module.purchase_request.purchase_request_model.purchase_details;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by sharvari on 20/6/18.
 */

public class DetailMessageItem extends RealmObject {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
