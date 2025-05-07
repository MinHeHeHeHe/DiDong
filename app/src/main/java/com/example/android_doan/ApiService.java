package com.example.android_doan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiService {
    @POST("api/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/user/logout")
    Call<LogoutResponse> logout();

    @POST("api/user/forget-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @GET("api/pizza/getAllPizza")
    Call<List<Pizza>> getAllPizzas();

    @POST("api/pizza/createPizza")
    Call<Pizza> createPizza(@Body Pizza request);
}