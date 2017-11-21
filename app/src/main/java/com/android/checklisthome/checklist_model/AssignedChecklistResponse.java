package com.android.checklisthome.checklist_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AssignedChecklistResponse extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @SerializedName("page_id")
    private int pageId;
    @SerializedName("assignedChecklistData")
    private AssignedChecklistData assignedChecklistData;
    @SerializedName("message")
    private String message;

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public int getPageId() {
        return pageId;
    }

    public void setAssignedChecklistData(AssignedChecklistData assignedChecklistData) {
        this.assignedChecklistData = assignedChecklistData;
    }

    public AssignedChecklistData getAssignedChecklistData() {
        return assignedChecklistData;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}