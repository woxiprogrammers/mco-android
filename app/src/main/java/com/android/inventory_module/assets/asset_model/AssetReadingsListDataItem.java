package com.android.inventory_module.assets.asset_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetReadingsListDataItem extends RealmObject {
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    private int inventoryComponentId, passMonth, passYear;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getFuelUsed() {
        return fuelUsed;
    }

    public void setFuelUsed(float fuelUsed) {
        this.fuelUsed = fuelUsed;
    }

    public int getUnitsUsed() {
        return unitsUsed;
    }

    public void setUnitsUsed(int unitsUsed) {
        this.unitsUsed = unitsUsed;
    }

    public int getElectricityUsed() {
        return electricityUsed;
    }

    public void setElectricityUsed(int electricityUsed) {
        this.electricityUsed = electricityUsed;
    }

    public int getTotalWorkingHours() {
        return totalWorkingHours;
    }

    public void setTotalWorkingHours(int totalWorkingHours) {
        this.totalWorkingHours = totalWorkingHours;
    }

    public int getTotalTopUp() {
        return totalTopUp;
    }

    public void setTotalTopUp(int totalTopUp) {
        this.totalTopUp = totalTopUp;
    }

    public int getInventoryComponentId() {
        return inventoryComponentId;
    }

    public void setInventoryComponentId(int inventoryComponentId) {
        this.inventoryComponentId = inventoryComponentId;
    }

    public int getPassMonth() {
        return passMonth;
    }

    public void setPassMonth(int passMonth) {
        this.passMonth = passMonth;
    }

    public int getPassYear() {
        return passYear;
    }

    public void setPassYear(int passYear) {
        this.passYear = passYear;
    }
}