package com.example.android_doan;

import com.google.gson.annotations.SerializedName;

public class ThemThongTinCaNhanResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("user")
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

    public static class User {
        @SerializedName("_id")
        private String id;
        private String username;
        private String email;
        private String address;
        @SerializedName("date_of_birth")
        private String dateOfBirth;
        private String phone;
        private String role;

        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }
        public String getDateOfBirth() { return dateOfBirth; }
        public String getPhone() { return phone; }
        public String getRole() { return role; }
    }
}