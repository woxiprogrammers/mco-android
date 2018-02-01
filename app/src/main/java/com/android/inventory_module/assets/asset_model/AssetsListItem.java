package com.android.inventory_module.assets.asset_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetsListItem extends RealmObject {
    @SerializedName("assets_name")
    private String assetsName;
    @SerializedName("asset_id")
    private int asset_id;
    @PrimaryKey
    @SerializedName("inventory_component_id")
    private int id;
    @SerializedName("assets_units")
    private String assetsUnits;
    @SerializedName("total_work_hour")
    private String totalWorkHour;
    @SerializedName("model_number")
    private String modelNumber;
    @SerializedName("total_diesel_consume")
    private String totalDieselConsume;
    @SerializedName("litre_per_unit")
    private float litrePerUnit;
    @SerializedName("electricity_per_unit")
    private float electricityPerUnit;
    @SerializedName("slug")
    private String slug;
    @SerializedName("total_electricity_consumed")
    private float totalElectricityConsumed;
    @SerializedName("in")
    private float in;
    @SerializedName("out")
    private float out;
    @SerializedName("available")
    private float available;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getTotalDieselConsume() {
        return totalDieselConsume;
    }

    public void setTotalDieselConsume(String totalDieselConsume) {
        this.totalDieselConsume = totalDieselConsume;
    }

    public float getLitrePerUnit() {
        return litrePerUnit;
    }

    public void setLitrePerUnit(float litrePerUnit) {
        this.litrePerUnit = litrePerUnit;
    }

    public float getElectricityPerUnit() {
        return electricityPerUnit;
    }

    public void setElectricityPerUnit(float electricityPerUnit) {
        this.electricityPerUnit = electricityPerUnit;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public float getTotalElectricityConsumed() {
        return totalElectricityConsumed;
    }

    public void setTotalElectricityConsumed(float totalElectricityConsumed) {
        this.totalElectricityConsumed = totalElectricityConsumed;
    }

    public float getIn() {
        return in;
    }

    public void setIn(float in) {
        this.in = in;
    }

    public float getOut() {
        return out;
    }

    public void setOut(float out) {
        this.out = out;
    }

    public float getAvailable() {
        return available;
    }

    public void setAvailable(float available) {
        this.available = available;
    }

    public int getCurrentSiteId() {
        return currentSiteId;
    }

    public void setCurrentSiteId(int currentSiteId) {
        this.currentSiteId = currentSiteId;
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

    public int getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(int asset_id) {
        this.asset_id = asset_id;
    }
}