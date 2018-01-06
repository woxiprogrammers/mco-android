package com.android.checklist_module.checklist_model.checklist_floor;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class FloorListItem extends RealmObject {
    @SerializedName("quotation_floor_name")
    private String quotationFloorName;
    @SerializedName("quotation_floor_id")
    private int quotationFloorId;

    public String getQuotationFloorName() {
        return quotationFloorName;
    }

    public void setQuotationFloorName(String quotationFloorName) {
        this.quotationFloorName = quotationFloorName;
    }

    public int getQuotationFloorId() {
        return quotationFloorId;
    }

    public void setQuotationFloorId(int quotationFloorId) {
        this.quotationFloorId = quotationFloorId;
    }
}