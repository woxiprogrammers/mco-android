package com.android.dashboard;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SitecountData extends RealmObject{

	@SerializedName("site_count_list")
	private RealmList<SiteCountListItem> siteCountList;

	public void setSiteCountList(RealmList<SiteCountListItem> siteCountList){
		this.siteCountList = siteCountList;
	}

	public RealmList<SiteCountListItem> getSiteCountList(){
		return siteCountList;
	}
}