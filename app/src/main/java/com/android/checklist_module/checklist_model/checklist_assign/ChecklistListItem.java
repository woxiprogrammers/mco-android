package com.android.checklist_module.checklist_model.checklist_assign;

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
    private int totalCheckpoints;
    @SerializedName("completed_checkpoints")
    private int completedCheckPoints;
    @SerializedName("assigned_to_user_name")
    private String assignedToUserName;
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
    @SerializedName("assigned_to")
    private int assignedTo;
    @SerializedName("assigned_by_user_name")
    private String assignedByUserName;
    @SerializedName("assigned_by")
    private int assignedBy;
    @SerializedName("reviewed_by_user_name")
    private String reviewedByUserName;
    @SerializedName("reviewed_by")
    private int reviewedBy;

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getAssignedOn() {
        return assignedOn;
    }

    public void setAssignedOn(String assignedOn) {
        this.assignedOn = assignedOn;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getProjectSiteChecklistId() {
        return projectSiteChecklistId;
    }

    public void setProjectSiteChecklistId(String projectSiteChecklistId) {
        this.projectSiteChecklistId = projectSiteChecklistId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalCheckpoints() {
        return totalCheckpoints;
    }

    public void setTotalCheckpoints(int totalCheckpoints) {
        this.totalCheckpoints = totalCheckpoints;
    }

    public int getCompletedCheckPoints() {
        return completedCheckPoints;
    }

    public void setCompletedCheckPoints(int completedCheckPoints) {
        this.completedCheckPoints = completedCheckPoints;
    }

    public String getAssignedToUserName() {
        return assignedToUserName;
    }

    public void setAssignedToUserName(String assignedToUserName) {
        this.assignedToUserName = assignedToUserName;
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

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedByUserName() {
        return assignedByUserName;
    }

    public void setAssignedByUserName(String assignedByUserName) {
        this.assignedByUserName = assignedByUserName;
    }

    public int getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(int assignedBy) {
        this.assignedBy = assignedBy;
    }

    public String getReviewedByUserName() {
        return reviewedByUserName;
    }

    public void setReviewedByUserName(String reviewedByUserName) {
        this.reviewedByUserName = reviewedByUserName;
    }

    public int getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(int reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
}