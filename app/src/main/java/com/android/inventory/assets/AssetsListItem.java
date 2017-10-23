package com.android.inventory.assets;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetsListItem extends RealmObject {
    @SerializedName("total_diesel_consume")
    private String totalDieselConsume;
    @PrimaryKey
    @SerializedName("inventory_component_id")
    private int id;
    @SerializedName("model_number")
    private String modelNumber;
    @SerializedName("total_work_hour")
    private String totalWorkHour;
    @SerializedName("is_diesel")
    private boolean isDiesel;
    @SerializedName("assets_units")
    private String assetsUnits;
    @SerializedName("assets_name")
    private String assetsName;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getTotalDieselConsume() {
        return totalDieselConsume;
    }

    public void setTotalDieselConsume(String totalDieselConsume) {
        this.totalDieselConsume = totalDieselConsume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getTotalWorkHour() {
        return totalWorkHour;
    }

    public void setTotalWorkHour(String totalWorkHour) {
        this.totalWorkHour = totalWorkHour;
    }

    public boolean isIsDiesel() {
        return isDiesel;
    }

    public void setIsDiesel(boolean isDiesel) {
        this.isDiesel = isDiesel;
    }

    public String getAssetsUnits() {
        return assetsUnits;
    }

    public void setAssetsUnits(String assetsUnits) {
        this.assetsUnits = assetsUnits;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }
}