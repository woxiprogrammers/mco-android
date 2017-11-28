package com.android.models.purchase_order;

import com.android.purchase_details.ImageItem;
import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PurchaseOrderListItem extends RealmObject {
    /*
        "data": {
        "purchase_order_list": [
        {
            "purchase_order_id": 1,
                "purchase_order_format_id": "PO21710071",
                "purchase_request_id": 11,
                "purchase_request_format_id": "PR21710071",
                "vendor_id": 1,
                "vendor_name": "Cement Vala Bhaiya",
                "client_name": "M/s Karia Realty",
                "project": "Konark Orchid",
                "date": "2017-10-07 12:09:00",
                "materials": "AGGREGATE 10 MM, RUBBLE(1 CUM Has 25% Voids)",
                "status": "Disapproved"
        }*/
    @PrimaryKey
    @SerializedName("purchase_order_id")
    private int id;
    @SerializedName("purchase_order_format_id")
    private String purchaseOrderFormatId;
    @SerializedName("purchase_request_id")
    private int purchaseRequestId;
    @SerializedName("purchase_request_format_id")
    private String purchaseRequestFormatId;
    @SerializedName("vendor_id")
    private int vendorId;
    @SerializedName("vendor_name")
    private String vendorName;
    @SerializedName("client_name")
    private String clientName;
    @SerializedName("project")
    private String project;
    @SerializedName("date")
    private String date;
    @SerializedName("materials")
    private String materials;
    @SerializedName("status")
    private String status;
    @SerializedName("grn_generated")
    private String grnGenerated;

    @SerializedName("images")
    private RealmList<ImageItem> listOfImages;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getDate() {
        return date;
    }

    public String getGrnGenerated() {
        return grnGenerated;
    }

    public void setGrnGenerated(String grnGenerated) {
        this.grnGenerated = grnGenerated;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(int purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurchaseOrderFormatId() {
        return purchaseOrderFormatId;
    }

    public void setPurchaseOrderFormatId(String purchaseOrderFormatId) {
        this.purchaseOrderFormatId = purchaseOrderFormatId;
    }

    public String getPurchaseRequestFormatId() {
        return purchaseRequestFormatId;
    }

    public void setPurchaseRequestFormatId(String purchaseRequestFormatId) {
        this.purchaseRequestFormatId = purchaseRequestFormatId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public RealmList<ImageItem> getListOfImages() {
        return listOfImages;
    }

    public void setListOfImages(RealmList<ImageItem> listOfImages) {
        this.listOfImages = listOfImages;
    }
}