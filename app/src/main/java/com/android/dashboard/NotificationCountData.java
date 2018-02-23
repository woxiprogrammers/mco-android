package com.android.dashboard;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class NotificationCountData extends RealmObject{

	@SerializedName("projectsNotificationCount")
	private RealmList<ProjectsNotificationCountItem> projectsNotificationCount;

	public void setProjectsNotificationCount(RealmList<ProjectsNotificationCountItem> projectsNotificationCount){
		this.projectsNotificationCount = projectsNotificationCount;
	}

	public RealmList<ProjectsNotificationCountItem> getProjectsNotificationCount(){
		return projectsNotificationCount;
	}
}