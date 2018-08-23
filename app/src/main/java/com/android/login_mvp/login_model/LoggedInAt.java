package com.android.login_mvp.login_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class LoggedInAt extends RealmObject {
    @SerializedName("date")
    private String date;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("timezone_type")
    private int timezoneType;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getTimezoneType() {
        return timezoneType;
    }

    public void setTimezoneType(int timezoneType) {
        this.timezoneType = timezoneType;
    }
}