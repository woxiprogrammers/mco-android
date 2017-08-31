package com.android.inventory.assets;
import com.google.gson.annotations.SerializedName;
public class AssetListResponse{

	@SerializedName("next_url")
	private String nextUrl;

	@SerializedName("message")
	private String message;

	@SerializedName("asset_response_data")
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