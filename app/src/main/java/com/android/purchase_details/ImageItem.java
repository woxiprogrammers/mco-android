package com.android.purchase_details;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by woxi-007 on 13/9/17.
 */

public class ImageItem extends RealmObject {

    public ImageItem(){

    }
    @SerializedName("image_url")
    private String imageUrl;
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
