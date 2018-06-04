package com.android.purchase_module.purchase_request.purchase_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
class ImagePurchaseOrderRequest extends RealmObject{

    @SerializedName("path")
    private String imagePath;

    public void setImagePath(String imageUrl){
        this.imagePath = imageUrl;
    }

    public String getImagePath(){
        return imagePath;
    }
}

