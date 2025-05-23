package com.example.android_doan.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;
    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("date_of_birth")
    private String dob;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setDob(String dob) {
        this.dob = dob;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
