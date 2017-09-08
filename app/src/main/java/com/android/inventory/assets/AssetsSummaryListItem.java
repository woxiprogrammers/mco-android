package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetsSummaryListItem extends RealmObject{

	@SerializedName("start_time")
	private String startTime;

	@SerializedName("stop_time")
	private String stopTime;

	@SerializedName("work_hour_in_day")
	private String workHourInDay;

	@SerializedName("top_up_time")
	private String topUpTime;

	@SerializedName("fuel_remaining")
	private int fuelRemaining;

	@SerializedName("total_diesel_consume")
	private int totalDieselConsume;

	@SerializedName("id")
	private int id;

	@SerializedName("assets_units")
	private String assetsUnits;

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

	public void setWorkHourInDay(String workHourInDay){
		this.workHourInDay = workHourInDay;
	}

	public String getWorkHourInDay(){
		return workHourInDay;
	}

	public void setTopUpTime(String topUpTime){
		this.topUpTime = topUpTime;
	}

	public String getTopUpTime(){
		return topUpTime;
	}

	public void setFuelRemaining(int fuelRemaining){
		this.fuelRemaining = fuelRemaining;
	}

	public int getFuelRemaining(){
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

	public void setAssetsUnits(String assetsUnits){
		this.assetsUnits = assetsUnits;
	}

	public String getAssetsUnits(){
		return assetsUnits;
	}
}