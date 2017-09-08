package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetSummaryListResponse extends RealmObject{

	@SerializedName("asset_name")
	private String assetName;

	@SerializedName("assets_summary_data")
	private AssetsSummaryData assetsSummaryData;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	public void setAssetName(String assetName){
		this.assetName = assetName;
	}

	public String getAssetName(){
		return assetName;
	}

	public void setAssetsSummaryData(AssetsSummaryData assetsSummaryData){
		this.assetsSummaryData = assetsSummaryData;
	}

	public AssetsSummaryData getAssetsSummaryData(){
		return assetsSummaryData;
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