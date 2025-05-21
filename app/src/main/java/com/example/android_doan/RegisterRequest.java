package com.example.android_doan;

public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String address;
    private String date_of_birth;
    private String phone;
    private String role;

    public RegisterRequest(String username, String password, String email, String address, String date_of_birth, String phone, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.date_of_birth = date_of_birth;
        this.phone = phone;
        this.role = role;
    }
}

