package com.example.android_doan;

public class LoginResponse {
    private String token;
    private User user;

    public static class User {
        private String id;
        private String username;
        private String email;
        private String role;

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
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