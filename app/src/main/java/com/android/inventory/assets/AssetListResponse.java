package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssetListResponse extends RealmObject {
	@PrimaryKey
	private int intIndex = 0;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private AssetResponseData assetResponseData;

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

	public void setAssetResponseData(AssetResponseData assetResponseData){
		this.assetResponseData = assetResponseData;
	}

	public AssetResponseData getAssetResponseData(){
		return assetResponseData;
	}
}