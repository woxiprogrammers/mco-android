package com.android.purchase_module.purchase_request.purchase_request_model.purchase_details;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Sharvari on 13/9/17.
 */
public class ImageItem extends RealmObject {
    @SerializedName("image_url")
    private String imageUrl;

    public ImageItem() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
