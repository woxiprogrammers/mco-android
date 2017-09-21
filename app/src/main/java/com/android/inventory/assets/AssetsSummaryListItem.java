package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetsSummaryListItem extends RealmObject{

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

	@SerializedName("id")
	private int id;

	@SerializedName("assets_units")
	private int assetsUnits;

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setStopTime(String stopTime){
		this.stopTime = stopTime;
	}

	public String getStopTime(){
		return stopTime;
	}

	public void setWorkHourInDay(int workHourInDay){
		this.workHourInDay = workHourInDay;
	}

	public int getWorkHourInDay(){
		return workHourInDay;
	}

	public void setTopUpTime(String topUpTime){
		this.topUpTime = topUpTime;
	}

	public String getTopUpTime(){
		return topUpTime;
	}

	public void setFuelRemaining(String fuelRemaining){
		this.fuelRemaining = fuelRemaining;
	}

	public String getFuelRemaining(){
		return fuelRemaining;
	}

	public void setTotalDieselConsume(int totalDieselConsume){
		this.totalDieselConsume = totalDieselConsume;
	}

	public int getTotalDieselConsume(){
		return totalDieselConsume;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAssetsUnits(int assetsUnits){
		this.assetsUnits = assetsUnits;
	}

	public int getAssetsUnits(){
		return assetsUnits;
	}
}