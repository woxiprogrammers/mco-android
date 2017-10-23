package com.android.inventory.assets;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetsSummaryListItem extends RealmObject {
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("stop_time")
    private String stopTime;
    @SerializedName("work_hour_in_day")
    private int workHourInDay;
    @SerializedName("top_up_time")
    private String topUpTime;
    @SerializedName("fuel_remaining")
    private String fuelRemaining;
    @SerializedName("total_diesel_consume")
    private int totalDieselConsume;
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("assets_units")
    private int assetsUnits;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public int getWorkHourInDay() {
        return workHourInDay;
    }

    public void setWorkHourInDay(int workHourInDay) {
        this.workHourInDay = workHourInDay;
    }

    public String getTopUpTime() {
        return topUpTime;
    }

    public void setTopUpTime(String topUpTime) {
        this.topUpTime = topUpTime;
    }

    public String getFuelRemaining() {
        return fuelRemaining;
    }

    public void setFuelRemaining(String fuelRemaining) {
        this.fuelRemaining = fuelRemaining;
    }

    public int getTotalDieselConsume() {
        return totalDieselConsume;
    }

    public void setTotalDieselConsume(int totalDieselConsume) {
        this.totalDieselConsume = totalDieselConsume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssetsUnits() {
        return assetsUnits;
    }

    public void setAssetsUnits(int assetsUnits) {
        this.assetsUnits = assetsUnits;
    }
}