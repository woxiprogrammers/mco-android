package com.android.inventory_module.inventory_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MaterialListItem extends RealmObject {
    @SerializedName("quantity_out")
    private String quantityOut;
    @PrimaryKey
    @SerializedName("id")
    private int id;
    /*@SerializedName("unit_id")
    private int unitId;
    @SerializedName("unit_name")
    private String unitName;*/
    @SerializedName("quantity_in")
    private String quantityIn;
    @SerializedName("material_name")
    private String materialName;
    @SerializedName("quantity_available")
    private String quantityAvailable;
    @SerializedName("units")
    private RealmList<Units> unitsRealmList;
    @SerializedName(("error_message"))
    private String errorMessage;
    private boolean isSelected;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public boolean isSelected() {
        return isSelected;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getQuantityOut() {
        return quantityOut;
    }

    public void setQuantityOut(String quantityOut) {
        this.quantityOut = quantityOut;
    }

    public String getQuantityIn() {
        return quantityIn;
    }

    public void setQuantityIn(String quantityIn) {
        this.quantityIn = quantityIn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(String quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
}