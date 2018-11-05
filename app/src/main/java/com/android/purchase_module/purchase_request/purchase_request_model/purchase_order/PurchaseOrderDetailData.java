package com.android.purchase_module.purchase_request.purchase_request_model.purchase_order;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PurchaseOrderDetailData extends RealmObject {
    @SerializedName("date")
    private String date;
    @SerializedName("vendor_mobile")
    private String vendorMobile;
    @SerializedName("materials")
    private RealmList<MaterialsItem> materials;
    @SerializedName("purchase_order_id")
    private int purchaseOrderId;
    @SerializedName("vendor_id")
    private int vendorId;
    @SerializedName("vendor_name")
    private String vendorName;
    @SerializedName("purchase_order_format_id")
    private String purchaseOrderFormatId;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setVendorMobile(String vendorMobile) {
        this.vendorMobile = vendorMobile;
    }

    public String getVendorMobile() {
        return vendorMobile;
    }

    public void setMaterials(RealmList<MaterialsItem> materials) {
        this.materials = materials;
    }

    public RealmList<MaterialsItem> getMaterials() {
        return materials;
    }

    public void setPurchaseOrderId(int purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public int getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setPurchaseOrderFormatId(String purchaseOrderFormatId) {
        this.purchaseOrderFormatId = purchaseOrderFormatId;
    }

    public String getPurchaseOrderFormatId() {
        return purchaseOrderFormatId;
    }
}