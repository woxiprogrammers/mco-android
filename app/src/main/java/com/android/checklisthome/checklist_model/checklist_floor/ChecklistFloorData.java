package com.android.checklisthome.checklist_model.checklist_floor;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ChecklistFloorData extends RealmObject {
    @SerializedName("floor_list")
    private RealmList<FloorListItem> floorList;

    public void setFloorList(RealmList<FloorListItem> floorList) {
        this.floorList = floorList;
    }

    public RealmList<FloorListItem> getFloorList() {
        return floorList;
    }
}