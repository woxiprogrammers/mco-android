package com.android.purchase_module.purchase_request.purchase_request_model.purchase_details;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemListItem extends RealmObject {
    /*"material_request_component_id": 42,
            "material_request_component_format_id": "MRM21710071",
            "material_request_id": 23,
            "material_request_format": "MR21710071",
            "name": "AGGREGATE 10 MM",
            "quantity": "1",
            "unit_id": 2,
            "unit_name": "BRASS(AGGREGATE)",

            "list_of_images": [
    {
        "image_url": null
    }*/

    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("material_request_component_format_id")
    private String materialRequestComponentFormatId;
    @SerializedName("material_request_id")
    private int materialRequestId;
    @SerializedName("unit_id")
    private int unitId;
    @SerializedName("material_request_format")
    private String materialRequestFormat;
    @SerializedName("unit_name")
    private String itemUnit;
    @SerializedName("list_of_images")
    private RealmList<ImageItem> listOfImages;
    @SerializedName("history_messages")
    private RealmList<DetailMessageItem> historyMessage;
    @SerializedName("name")
    private String itemName;
    @PrimaryKey
    @SerializedName("material_request_component_id")
    private int id;
    @SerializedName("quantity")
    private float itemQuantity;
    @SerializedName("purchase_request_id")
    private int purchaseRequestId;
    @SerializedName("disapproved_by_user_name")
    private String disapprovedByUserName;

    public String getDisapprovedByUserName() {
        return disapprovedByUserName;
    }

    public void setDisapprovedByUserName(String disapprovedByUserName) {
        this.disapprovedByUserName = disapprovedByUserName;
    }

    public int getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(int purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public RealmList<DetailMessageItem> getHistoryMessage() {
        return historyMessage;
    }

    public void setHistoryMessage(RealmList<DetailMessageItem> historyMessage) {
        this.historyMessage = historyMessage;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public RealmList<ImageItem> getListOfImages() {
        return listOfImages;
    }

    public void setListOfImages(RealmList<ImageItem> listOfImages) {
        this.listOfImages = listOfImages;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(float itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public int getMaterialRequestId() {
        return materialRequestId;
    }

    public void setMaterialRequestId(int materialRequestId) {
        this.materialRequestId = materialRequestId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getMaterialRequestFormat() {
        return materialRequestFormat;
    }

    public void setMaterialRequestFormat(String materialRequestFormat) {
        this.materialRequestFormat = materialRequestFormat;
    }

    public String getMaterialRequestComponentFormatId() {
        return materialRequestComponentFormatId;
    }

    public void setMaterialRequestComponentFormatId(String materialRequestComponentFormatId) {
        this.materialRequestComponentFormatId = materialRequestComponentFormatId;
    }
}