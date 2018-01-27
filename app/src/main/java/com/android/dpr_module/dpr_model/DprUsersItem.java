package com.android.dpr_module.dpr_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DprUsersItem extends RealmObject {
    private int intPrimaryKey; //Required
    private String strDate;
    private String strSubConName;
    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);
    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("cat")
    private String cat;
    @SerializedName("no_of_users")
    private int noOfUsers;

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getCat() {
        return cat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNoOfUsers(int noOfUsers) {
        this.noOfUsers = noOfUsers;
    }

    public int getNoOfUsers() {
        return noOfUsers;
    }

    public int getIntPrimaryKey() {
        return intPrimaryKey;
    }

    public void setIntPrimaryKey(int intPrimaryKey) {
        this.intPrimaryKey = intPrimaryKey;
    }

    public String getStrSubConName() {
        return strSubConName;
    }

    public void setStrSubConName(String strSubConName) {
        this.strSubConName = strSubConName;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }
}