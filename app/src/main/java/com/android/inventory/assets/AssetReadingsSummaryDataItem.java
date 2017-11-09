package com.android.inventory.assets;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Sharvari on 9/11/17.
 */

public class AssetReadingsSummaryDataItem extends RealmObject {
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("date")
    private String date;
    ///////////////////
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

    public void setStartReading(String startReading) {
        this.startReading = startReading;
    }

    public String getStartReading() {
        return startReading;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStopReading(String stopReading) {
        this.stopReading = stopReading;
    }

    public String getStopReading() {
        return stopReading;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setElectricityPerUnit(String electricityPerUnit) {
        this.electricityPerUnit = electricityPerUnit;
    }

    public String getElectricityPerUnit() {
        return electricityPerUnit;
    }

    public void setTopUpTime(String topUpTime) {
        this.topUpTime = topUpTime;
    }

    public String getTopUpTime() {
        return topUpTime;
    }

    public void setTopUp(String topUp) {
        this.topUp = topUp;
    }

    public String getTopUp() {
        return topUp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFuelPerUnit(String fuelPerUnit) {
        this.fuelPerUnit = fuelPerUnit;
    }

    public String getFuelPerUnit() {
        return fuelPerUnit;
    }
    ///////////////////

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}