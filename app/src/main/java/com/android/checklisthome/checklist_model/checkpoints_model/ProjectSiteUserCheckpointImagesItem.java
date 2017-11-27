package com.android.checklisthome.checklist_model.checkpoints_model;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ProjectSiteUserCheckpointImagesItem extends RealmObject{

	@SerializedName("project_site_checklist_checkpoint_image_caption")
	private String projectSiteChecklistCheckpointImageCaption;

	@SerializedName("project_site_checklist_checkpoint_image_is_required")
	private boolean projectSiteChecklistCheckpointImageIsRequired;

	@SerializedName("project_site_checklist_checkpoint_image_id")
	private int projectSiteChecklistCheckpointImageId;

	public void setProjectSiteChecklistCheckpointImageCaption(String projectSiteChecklistCheckpointImageCaption){
		this.projectSiteChecklistCheckpointImageCaption = projectSiteChecklistCheckpointImageCaption;
	}

	public String getProjectSiteChecklistCheckpointImageCaption(){
		return projectSiteChecklistCheckpointImageCaption;
	}

	public void setProjectSiteChecklistCheckpointImageIsRequired(boolean projectSiteChecklistCheckpointImageIsRequired){
		this.projectSiteChecklistCheckpointImageIsRequired = projectSiteChecklistCheckpointImageIsRequired;
	}

	public boolean isProjectSiteChecklistCheckpointImageIsRequired(){
		return projectSiteChecklistCheckpointImageIsRequired;
	}

	public void setProjectSiteChecklistCheckpointImageId(int projectSiteChecklistCheckpointImageId){
		this.projectSiteChecklistCheckpointImageId = projectSiteChecklistCheckpointImageId;
	}

	public int getProjectSiteChecklistCheckpointImageId(){
		return projectSiteChecklistCheckpointImageId;
	}
}