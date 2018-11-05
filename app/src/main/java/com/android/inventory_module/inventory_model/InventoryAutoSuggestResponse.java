package com.android.inventory_module.inventory_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class InventoryAutoSuggestResponse extends RealmObject {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private RealmList<AutoSuggestdataItem> autoSuggestdata;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RealmList<AutoSuggestdataItem> getAutoSuggestdata() {
        return autoSuggestdata;
    }

    public void setAutoSuggestdata(RealmList<AutoSuggestdataItem> autoSuggestdata) {
        this.autoSuggestdata = autoSuggestdata;
    }
}