package com.android.checklisthome.checklist_model.reassign_checkpoints;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class ReassignCheckpointsResponse extends RealmObject {
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private ReassignCheckpointsData reassignCheckpointsData;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setReassignCheckpointsData(ReassignCheckpointsData reassignCheckpointsData) {
        this.reassignCheckpointsData = reassignCheckpointsData;
    }

    public ReassignCheckpointsData getReassignCheckpointsData() {
        return reassignCheckpointsData;
    }
}