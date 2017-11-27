package com.android.checklisthome.checklist_model.checkpoints_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CheckPointsItem extends RealmObject{

	@SerializedName("project_site_user_checkpoint_images")
	private RealmList<ProjectSiteUserCheckpointImagesItem> projectSiteUserCheckpointImages;

	@PrimaryKey
	@SerializedName("project_site_user_checkpoint_id")
	private int projectSiteUserCheckpointId;

	@SerializedName("project_site_user_checkpoint_description")
	private String projectSiteUserCheckpointDescription;

	@SerializedName("project_site_user_checkpoint_is_ok")
	private boolean projectSiteUserCheckpointIsOk;

	public void setProjectSiteUserCheckpointImages(RealmList<ProjectSiteUserCheckpointImagesItem> projectSiteUserCheckpointImages){
		this.projectSiteUserCheckpointImages = projectSiteUserCheckpointImages;
	}

	public RealmList<ProjectSiteUserCheckpointImagesItem> getProjectSiteUserCheckpointImages(){
		return projectSiteUserCheckpointImages;
	}

	public void setProjectSiteUserCheckpointId(int projectSiteUserCheckpointId){
		this.projectSiteUserCheckpointId = projectSiteUserCheckpointId;
	}

	public int getProjectSiteUserCheckpointId(){
		return projectSiteUserCheckpointId;
	}

	public void setProjectSiteUserCheckpointDescription(String projectSiteUserCheckpointDescription){
		this.projectSiteUserCheckpointDescription = projectSiteUserCheckpointDescription;
	}

	public String getProjectSiteUserCheckpointDescription(){
		return projectSiteUserCheckpointDescription;
	}

	public void setProjectSiteUserCheckpointIsOk(boolean projectSiteUserCheckpointIsOk){
		this.projectSiteUserCheckpointIsOk = projectSiteUserCheckpointIsOk;
	}

	public boolean isProjectSiteUserCheckpointIsOk(){
		return projectSiteUserCheckpointIsOk;
	}
}