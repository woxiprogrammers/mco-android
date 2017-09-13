package com.android.inventory.assets;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AssetsSummaryData extends RealmObject{

	@SerializedName("assets_summary_list")
	private RealmList<AssetsSummaryListItem> assetsSummaryList;

	public void setAssetsSummaryList(RealmList<AssetsSummaryListItem> assetsSummaryList){
		this.assetsSummaryList = assetsSummaryList;
	}

	public RealmList<AssetsSummaryListItem> getAssetsSummaryList(){
		return assetsSummaryList;
	}
}