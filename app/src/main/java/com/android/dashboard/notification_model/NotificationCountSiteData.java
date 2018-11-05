package com.android.dashboard.notification_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class NotificationCountSiteData extends RealmObject{

	@SerializedName("projects")
	private RealmList<ProjectsNotificationCountItem> projectsNotificationCount;

	public void setProjectsNotificationCount(RealmList<ProjectsNotificationCountItem> projectsNotificationCount){
		this.projectsNotificationCount = projectsNotificationCount;
	}

	public RealmList<ProjectsNotificationCountItem> getProjectsNotificationCount(){
		return projectsNotificationCount;
	}
}