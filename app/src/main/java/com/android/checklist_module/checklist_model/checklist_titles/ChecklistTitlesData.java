package com.android.checklist_module.checklist_model.checklist_titles;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ChecklistTitlesData extends RealmObject {
    @SerializedName("title_list")
    private RealmList<TitleListItem> titleList;

    public RealmList<TitleListItem> getTitleList() {
        return titleList;
    }

    public void setTitleList(RealmList<TitleListItem> titleList) {
        this.titleList = titleList;
    }
}