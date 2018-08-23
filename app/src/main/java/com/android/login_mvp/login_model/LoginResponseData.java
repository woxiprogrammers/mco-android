package com.android.login_mvp.login_model;

import com.android.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class LoginResponseData extends RealmObject {
    @SerializedName("is_active")
    private boolean isActive;
    @SerializedName("projects")
    private RealmList<ProjectsItem> projects;
    @SerializedName("gender")
    private String gender;
    @SerializedName("dob")
    private String dob;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("email")
    private String email;
    @SerializedName("modules")
    private RealmList<ModulesItem> modules;

    @SerializedName("user_role")
    private String user_role;

    private int currentSiteId = AppUtils.getInstance().getInt("projectId", -1);

    public RealmList<ProjectsItem> getProjects() {
        return projects;
    }

    public void setProjects(RealmList<ProjectsItem> projects) {
        this.projects = projects;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RealmList<ModulesItem> getModules() {
        return modules;
    }

    public void setModules(RealmList<ModulesItem> modules) {
        this.modules = modules;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
}