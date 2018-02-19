package com.android.dashboard;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class SiteCountListItem extends RealmObject{

	@SerializedName("project_site_id")
	private int projectSiteId;

	@SerializedName("project_site_name")
	private String projectSiteName;

	@SerializedName("site_count")
	private int siteCount;

	public void setProjectSiteId(int projectSiteId){
		this.projectSiteId = projectSiteId;
	}

	public int getProjectSiteId(){
		return projectSiteId;
	}

	public void setProjectSiteName(String projectSiteName){
		this.projectSiteName = projectSiteName;
	}

	public String getProjectSiteName(){
		return projectSiteName;
	}

	public void setSiteCount(int siteCount){
		this.siteCount = siteCount;
	}

	public int getSiteCount(){
		return siteCount;
	}
}