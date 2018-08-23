package com.android.inventory_module.assets.asset_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sharvari on 9/11/17.
 */
public class AssetReadingsSummaryDataItem extends RealmObject {
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    private int inventoryComponentId;
    private String strDate;
    @SerializedName("date")
    private String date;
    @SerializedName("start_reading")
    private String startReading;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("stop_reading")
    private String stopReading;
    @SerializedName("stop_time")
    private String stopTime;
    @SerializedName("electricity_per_unit")
    private String electricityPerUnit;
    @SerializedName("top_up_time")
    private String topUpTime;
    @SerializedName("top_up")
    private String topUp;
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("fuel_per_unit")
    private String fuelPerUnit;

    public String getStartReading() {
        return startReading;
    }

    public void setStartReading(String startReading) {
        this.startReading = startReading;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopReading() {
        return stopReading;
    }

    public void setStopReading(String stopReading) {
        this.stopReading = stopReading;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getElectricityPerUnit() {
        return electricityPerUnit;
    }

    public void setElectricityPerUnit(String electricityPerUnit) {
        this.electricityPerUnit = electricityPerUnit;
    }

    public String getTopUpTime() {
        return topUpTime;
    }

    public void setTopUpTime(String topUpTime) {
        this.topUpTime = topUpTime;
    }

    public String getTopUp() {
        return topUp;
    }

    public void setTopUp(String topUp) {
        this.topUp = topUp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFuelPerUnit() {
        return fuelPerUnit;
    }

    public void setFuelPerUnit(String fuelPerUnit) {
        this.fuelPerUnit = fuelPerUnit;
    }
    ///////////////////

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getInventoryComponentId() {
        return inventoryComponentId;
    }

    public void setInventoryComponentId(int inventoryComponentId) {
        this.inventoryComponentId = inventoryComponentId;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }
}