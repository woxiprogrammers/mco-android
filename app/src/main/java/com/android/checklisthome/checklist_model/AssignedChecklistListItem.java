package com.android.checklisthome.checklist_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssignedChecklistListItem extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    private int id;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("floor_name")
    private String floorName;
    @SerializedName("assigned_on")
    private String assignedOn;
    @SerializedName("sub_category")
    private String subCategory;
    @SerializedName("checklist_id")
    private String checklistId;
    @SerializedName("description")
    private String description;
    @SerializedName("category")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("total_checkpounts")
    private int totalCheckpounts;
    @SerializedName("assigned_to")
    private String assignedTo;

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

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setChecklistId(String checklistId) {
        this.checklistId = checklistId;
    }

    public String getChecklistId() {
        return checklistId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
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

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedTo() {
        return assignedTo;
    }
}