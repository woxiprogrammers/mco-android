package com.android.purchase_module.purchase_request.purchase_request_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Sharvari.
 */
class PdfPurchaseOrderRequest extends RealmObject {

    @SerializedName("path")
    private String pdfPath;

    public void setPdfPath(String imageUrl){
        this.pdfPath = imageUrl;
    }

    public String getPdfPath(){
        return pdfPath;
    }
}
