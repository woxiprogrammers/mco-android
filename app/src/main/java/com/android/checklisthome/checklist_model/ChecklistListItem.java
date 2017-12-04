package com.android.checklisthome.checklist_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ChecklistListItem extends RealmObject {
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("floor_name")
    private String floorName;
    @SerializedName("assigned_on")
    private String assignedOn;
    @SerializedName("sub_category_name")
    private String subCategoryName;
    @SerializedName("project_site_checklist_id")
    private String projectSiteChecklistId;
    @SerializedName("description")
    private String description;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("title")
    private String title;
    @SerializedName("total_checkpoints")
    private int totalCheckpounts;
    @SerializedName("assigned_user_name")
    private String assignedUserName;
    @PrimaryKey
    @SerializedName("project_site_user_checklist_assignment_id")
    private int projectSiteUserChecklistAssignmentId;
    @SerializedName("category_id")
    private int categoryId;
    @SerializedName("sub_category_id")
    private int subCategoryId;
    @SerializedName("assigned_user")
    private int assignedUserId;
    @SerializedName("checklist_current_status")
    private String checklistCurrentStatus;

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setAssignedOn(String assignedOn) {
        this.assignedOn = assignedOn;
    }

    public String getAssignedOn() {
        return assignedOn;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setProjectSiteChecklistId(String projectSiteChecklistId) {
        this.projectSiteChecklistId = projectSiteChecklistId;
    }

    public String getProjectSiteChecklistId() {
        return projectSiteChecklistId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTotalCheckpounts(int totalCheckpounts) {
        this.totalCheckpounts = totalCheckpounts;
    }

    public int getTotalCheckpounts() {
        return totalCheckpounts;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public int getProjectSiteUserChecklistAssignmentId() {
        return projectSiteUserChecklistAssignmentId;
    }

    public void setProjectSiteUserChecklistAssignmentId(int projectSiteUserChecklistAssignmentId) {
        this.projectSiteUserChecklistAssignmentId = projectSiteUserChecklistAssignmentId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getChecklistCurrentStatus() {
        return checklistCurrentStatus;
    }

    public void setChecklistCurrentStatus(String checklistCurrentStatus) {
        this.checklistCurrentStatus = checklistCurrentStatus;
    }
}