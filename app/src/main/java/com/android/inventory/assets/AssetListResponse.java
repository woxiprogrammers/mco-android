package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class AssetListResponse extends RealmObject {

	@SerializedName("data")
	private AssetListData assetListData;

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	public void setAssetListData(AssetListData assetListData){
		this.assetListData = assetListData;
	}

	public AssetListData getAssetListData(){
		return assetListData;
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