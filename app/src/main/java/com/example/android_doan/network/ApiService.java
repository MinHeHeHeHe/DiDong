package com.example.android_doan.network;

import java.util.List;

import com.example.android_doan.ForgotPasswordRequest;
import com.example.android_doan.ForgotPasswordResponse;
import com.example.android_doan.LoginRequest;
import com.example.android_doan.LoginResponse;
import com.example.android_doan.ResetPasswordRequest;
import com.example.android_doan.ResetPasswordResponse;
import com.example.android_doan.ThemThongTinCaNhanRequest;
import com.example.android_doan.ThemThongTinCaNhanResponse;
import com.example.android_doan.VerifyCodeRequest;
import com.example.android_doan.VerifyCodeResponse;
import com.example.android_doan.ui.profile.LogoutResponse;
import com.example.android_doan.RegisterRequest;
import com.example.android_doan.RegisterResponse;
import com.example.android_doan.model.Pizza;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/user/createUser")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/user/logout")
    Call<LogoutResponse> logout();


    @POST("api/user/reset-password")
    @GET("api/pizza/getAllPizza")
    Call<List<Pizza>> getAllPizzas();

    @POST("api/pizza/createPizza")
    Call<Pizza> createPizza(@Body Pizza request);

    @POST("api/user/forget-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("api/user/verify-code")
    Call<VerifyCodeResponse> verifyCode(@Body VerifyCodeRequest request);

    @POST("api/user/reset-password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);

    @PUT("api/user/updateUser/{id}")
    Call<ThemThongTinCaNhanResponse> updateUser(
            @Header("Authorization") String token,
            @Path("id") String userId,
            @Body ThemThongTinCaNhanRequest request
    );

}