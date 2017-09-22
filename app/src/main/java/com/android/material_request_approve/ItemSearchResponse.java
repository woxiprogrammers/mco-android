package com.android.material_request_approve;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ItemSearchResponse extends RealmObject {
    @SerializedName("item_search_response_data")
    private ItemSearchResponseData itemSearchResponseData;
    @SerializedName("message")
    private String message;

    public void setItemSearchResponseData(ItemSearchResponseData itemSearchResponseData) {
        this.itemSearchResponseData = itemSearchResponseData;
    }

    public ItemSearchResponseData getItemSearchResponseData() {
        return itemSearchResponseData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}