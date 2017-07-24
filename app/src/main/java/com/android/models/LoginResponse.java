package com.android.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("data")
    private Data data;
    @JsonProperty("message")
    private String message;
    @JsonProperty("token")
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

    @Override
    public String toString() {
        return
                "LoginResponse{" +
                        "data = '" + data + '\'' +
                        ",message = '" + message + '\'' +
                        ",token = '" + token + '\'' +
                        "}";
    }

    /**
     * private class Data
     */
    private class Data {
        @JsonProperty("is_active")
        private boolean isActive;
        @JsonProperty("gender")
        private String gender;
        @JsonProperty("dob")
        private String dob;
        @JsonProperty("mobile")
        private String mobile;
        @JsonProperty("last_name")
        private String lastName;
        @JsonProperty("id")
        private int id;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("email")
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

        @Override
        public String toString() {
            return
                    "Data{" +
                            "is_active = '" + isActive + '\'' +
                            ",gender = '" + gender + '\'' +
                            ",dob = '" + dob + '\'' +
                            ",mobile = '" + mobile + '\'' +
                            ",last_name = '" + lastName + '\'' +
                            ",id = '" + id + '\'' +
                            ",first_name = '" + firstName + '\'' +
                            ",email = '" + email + '\'' +
                            "}";
        }
    }
}