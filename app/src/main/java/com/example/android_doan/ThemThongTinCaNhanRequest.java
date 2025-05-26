package com.example.android_doan;
import com.google.gson.annotations.SerializedName;

public class ThemThongTinCaNhanRequest {
    private String username;
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    private String address;
    private String phone;
    private String role;

    // Constructor
    public ThemThongTinCaNhanRequest(String username, String date_of_birth, String address, String phone, String role) {
        this.username = username;
        this.dateOfBirth = date_of_birth;
        this.address = address;
        this.phone = phone;
        this.role = role;
    }

    public ThemThongTinCaNhanRequest(String username, String date_of_birth, String phone, String address) {
        this.username = username;
        this.dateOfBirth = date_of_birth;
        this.address = address;
        this.phone = phone;

    }

}
