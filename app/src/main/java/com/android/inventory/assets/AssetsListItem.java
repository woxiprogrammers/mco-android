package com.android.inventory.assets;


import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;

public class AssetsListItem extends RealmObject {

	@SerializedName("id")
	private int id;

	@SerializedName("assets_units")
	private String assetsUnits;

	@SerializedName("assets_name")
	private String assetsName;

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

	public void setAssetsName(String assetsName){
		this.assetsName = assetsName;
	}

	public String getAssetsName(){
		return assetsName;
	}
}