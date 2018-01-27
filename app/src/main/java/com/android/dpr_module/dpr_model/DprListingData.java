package com.android.dpr_module.dpr_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DprListingData extends RealmObject {
    @PrimaryKey
    private int primaryKey = 0;
    @SerializedName("sub_contractor_list")
    private RealmList<DprListItem> dprList;

    public void setDprList(RealmList<DprListItem> dprList) {
        this.dprList = dprList;
    }

    public RealmList<DprListItem> getDprList() {
        return dprList;
    }
}