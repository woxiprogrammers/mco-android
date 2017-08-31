package com.android.inventory.assets;

import java.util.List;
import com.google.gson.annotations.SerializedName;
public class AssetResponseData{

	@SerializedName("assets_list")
	private List<AssetsListItem> assetsList;

	public void setAssetsList(List<AssetsListItem> assetsList){
		this.assetsList = assetsList;
	}

	public List<AssetsListItem> getAssetsList(){
		return assetsList;
	}
}