package com.android.inventory.assets;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AssetResponseData extends RealmObject {

	@SerializedName("assets_list")
	private RealmList<AssetsListItem> assetsList;

	public void setAssetsList(RealmList<AssetsListItem> assetsList){
		this.assetsList = assetsList;
	}

	public RealmList<AssetsListItem> getAssetsList(){
		return assetsList;
	}
}