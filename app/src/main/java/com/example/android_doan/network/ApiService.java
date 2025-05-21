package com.example.android_doan.network;

import java.util.List;

import com.example.android_doan.ForgotPasswordRequest;
import com.example.android_doan.ForgotPasswordResponse;
import com.example.android_doan.LoginRequest;
import com.example.android_doan.LoginResponse;
import com.example.android_doan.ResetPasswordRequest;
import com.example.android_doan.ResetPasswordResponse;
import com.example.android_doan.model.Drink;
import com.example.android_doan.ui.profile.LogoutResponse;
import com.example.android_doan.RegisterRequest;
import com.example.android_doan.RegisterResponse;
import com.example.android_doan.model.Pizza;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiService {
    @POST("api/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/user/createUser")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/user/logout")
    Call<LogoutResponse> logout();


    @GET("api/pizza/getAllPizza")
    Call<List<Pizza>> getAllPizzas();

    @GET("api/drink/getAllDrink")
    Call<List<Drink>> getAllDrinks();

    @POST("api/pizza/createPizza")
    Call<Pizza> createPizza(@Body Pizza request);

    @POST("api/user/forget-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    //@POST("api/user/verify-code")
    //Call<ForgotPasswordResponse> verifyCode(@Body String code);

    @POST("api/user/reset-password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);


}