package com.example.android_doan;

import com.google.gson.annotations.SerializedName;

public class VerifyCodeRequest {
    @SerializedName("token")  // phải trùng với key backend mong đợi
    private String token;

    public VerifyCodeRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
