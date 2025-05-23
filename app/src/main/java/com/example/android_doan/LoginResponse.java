package com.example.android_doan;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String token;
    private User user;

    public static class User {
        private String id;
        private String username;
        @SerializedName("image_url")
        private String imageUrl;
        private String dob;
        private String phone;
        private String address;
        private String email;
        private String role;

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }
        public String getImageUrl() {
            return imageUrl;
        }
        public String getDob() {
            return dob;
        }
        public String getPhone() {
            return phone;
        }
        public String getAddress() {
            return address;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}