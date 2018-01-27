package com.android.dpr_module.dpr_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class DPRSubContractorResponse extends RealmObject {
    @SerializedName("data")
    private RealmList<DprdataItem> dprdata;
    @SerializedName("message")
    private String message;

    public RealmList<DprdataItem> getDprdata() {
        return dprdata;
    }

    public void setDprdata(RealmList<DprdataItem> dprdata) {
        this.dprdata = dprdata;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}