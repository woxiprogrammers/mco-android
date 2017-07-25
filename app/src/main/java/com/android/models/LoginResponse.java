package com.android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class LoginResponse extends RealmObject {
    @SerializedName("data")
    private Data data;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    @Required
    private String token;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private class Data {
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
    }
}