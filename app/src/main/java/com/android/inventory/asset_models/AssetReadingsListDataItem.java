package com.android.inventory.asset_models;

import android.annotation.SuppressLint;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetReadingsListDataItem extends RealmObject {
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("date")
    private String date;
    @SerializedName("fuel_used")
    private int fuelUsed;
    @SerializedName("units_used")
    private int unitsUsed;
    @SerializedName("electricity_used")
    private int electricityUsed;
    @SerializedName("total_working_hours")
    private int totalWorkingHours;
    @SerializedName("total_top_up")
    private int totalTopUp;
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
    @SerializedName("id")
    private int id;
    @SerializedName("fuel_per_unit")
    private String fuelPerUnit;
    @PrimaryKey
    private String primaryKey = id + date;/* = new Random().nextInt((999999) + 11) + new Random().nextInt((999999) + 11);*/

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

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setFuelUsed(int fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    public int getFuelUsed() {
        return fuelUsed;
    }

    public void setUnitsUsed(int unitsUsed) {
        this.unitsUsed = unitsUsed;
    }

    public int getUnitsUsed() {
        return unitsUsed;
    }

    public void setElectricityUsed(int electricityUsed) {
        this.electricityUsed = electricityUsed;
    }

    public int getElectricityUsed() {
        return electricityUsed;
    }

    public void setTotalWorkingHours(int totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }

    public int getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalTopUp(int totalTopUp) {
        this.totalTopUp = totalTopUp;
    }

    public int getTotalTopUp() {
        return totalTopUp;
    }

    // local defined primary key
    @SuppressLint("DefaultLocale")
    public void compoundPrimaryKey() {
        if (!isManaged()) {
            // only un-managed objects needs compound key
            int randomNum = new Random().nextInt((999999) + 11);
            this.primaryKey = String.format("%s%d%d", this.date.replaceAll("[^a-zA-Z]+", ""), this.id, randomNum);
        }
    }
}