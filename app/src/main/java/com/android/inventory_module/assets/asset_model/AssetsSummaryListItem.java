package com.android.inventory_module.assets.asset_model;

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

    /*"date": "2017-11-04",
            "total_working_hours": 5,
            "fuel_used": -1,
            "electricity_used": -1,
            "units_used": 110,
            "total_top_up": -1
            "id": 6,
            "start_reading": "200",
            "stop_reading": "230",
            "start_time": "12:05",
            "stop_time": "14:06",
            "top_up_time": null,
            "top_up": null,
            "electricity_per_unit": "5",
            "fuel_per_unit": null*/

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