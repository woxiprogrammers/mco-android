package com.android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by woxi-007 on 18/8/17.
 */

public class MaterialListItem extends RealmObject{

    private String materialName;
    private String quantityIn;
    private String quantityOut;
    private String quantityCurrent;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getQuantityIn() {
        return quantityIn;
    }

    public void setQuantityIn(String quantityIn) {
        this.quantityIn = quantityIn;
    }

    public String getQuantityOut() {
        return quantityOut;
    }

    public void setQuantityOut(String quantityOut) {
        this.quantityOut = quantityOut;
    }

    public String getQuantityCurrent() {
        return quantityCurrent;
    }

    public void setQuantityCurrent(String quantityCurrent) {
        this.quantityCurrent = quantityCurrent;
    }
}
