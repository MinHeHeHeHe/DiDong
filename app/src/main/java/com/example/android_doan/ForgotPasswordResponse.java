package com.example.android_doan;

public class ForgotPasswordResponse {
    private String message;
    private String token; // token sent from server

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}