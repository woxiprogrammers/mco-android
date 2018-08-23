package com.android.purchase_module.purchase_request.purchase_request_model.purchase_request;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseRequestResponse extends RealmObject {
    @PrimaryKey
    private int intIndex = 0;
    @SerializedName("next_url")
    private String nextUrl;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private PurchaseRequestRespData purchaseRequestRespData;private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    @SerializedName("page_id")
    private String page_id;

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PurchaseRequestRespData getPurchaseRequestRespData() {
        return purchaseRequestRespData;
    }

    public void setPurchaseRequestRespData(PurchaseRequestRespData purchaseRequestRespData) {
        this.purchaseRequestRespData = purchaseRequestRespData;
    }
}