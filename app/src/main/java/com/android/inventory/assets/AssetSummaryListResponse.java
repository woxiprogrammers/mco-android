package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetSummaryListResponse extends RealmObject{

	@SerializedName("data")
	private AssetsSummaryData summaryData;

	@SerializedName("asset_name")
	private String assetName;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	public void setDummyData(AssetsSummaryData assetsSummaryData){
		this.summaryData = summaryData;
	}

	public AssetsSummaryData getDummyData(){
		return summaryData;
	}

	public void setAssetName(String assetName){
		this.assetName = assetName;
	}

	public String getAssetName(){
		return assetName;
	}

	public void setNextUrl(String nextUrl){
		this.nextUrl = nextUrl;
	}

	public String getNextUrl(){
		return nextUrl;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}