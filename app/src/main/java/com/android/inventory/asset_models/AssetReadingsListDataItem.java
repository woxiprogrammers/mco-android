package com.android.inventory.asset_models;

import android.annotation.SuppressLint;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetReadingsListDataItem extends RealmObject {
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @PrimaryKey
    @SerializedName("date")
    private String date;
    @SerializedName("fuel_used")
    private float fuelUsed;
    @SerializedName("units_used")
    private int unitsUsed;
    @SerializedName("electricity_used")
    private int electricityUsed;
    @SerializedName("total_working_hours")
    private int totalWorkingHours;
    @SerializedName("total_top_up")
    private int totalTopUp;
    ///////////////////

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setFuelUsed(float fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    public float getFuelUsed() {
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
}