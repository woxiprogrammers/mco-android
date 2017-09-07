package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetsListItem extends RealmObject{

	@SerializedName("total_diesel_consume")
	private int totalDieselConsume;

	@SerializedName("model_number")
	private String modelNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("total_work_hour")
	private String totalWorkHour;

	@SerializedName("assets_units")
	private String assetsUnits;

	@SerializedName("assets_name")
	private String assetsName;

	public void setTotalDieselConsume(int totalDieselConsume){
		this.totalDieselConsume = totalDieselConsume;
	}

	public int getTotalDieselConsume(){
		return totalDieselConsume;
	}

	public void setModelNumber(String modelNumber){
		this.modelNumber = modelNumber;
	}

	public String getModelNumber(){
		return modelNumber;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTotalWorkHour(String totalWorkHour){
		this.totalWorkHour = totalWorkHour;
	}

	public String getTotalWorkHour(){
		return totalWorkHour;
	}

	public void setAssetsUnits(String assetsUnits){
		this.assetsUnits = assetsUnits;
	}

	public String getAssetsUnits(){
		return assetsUnits;
	}

	public void setAssetsName(String assetsName){
		this.assetsName = assetsName;
	}

	public String getAssetsName(){
		return assetsName;
	}
}