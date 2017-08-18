package com.android.models.login_acl;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class LoginResponseData extends RealmObject {
    @SerializedName("projects")
    private RealmList<ProjectsItem> projects;
    @SerializedName("is_active")
    private boolean isActive;
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

    public void setProjects(RealmList<ProjectsItem> projects) {
        this.projects = projects;
    }

    public RealmList<ProjectsItem> getProjects() {
        return projects;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setModules(RealmList<ModulesItem> modules) {
        this.modules = modules;
    }

    public RealmList<ModulesItem> getModules() {
        return modules;
    }
}