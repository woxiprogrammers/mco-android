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
}