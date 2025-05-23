package com.example.android_doan.network;

import java.util.List;

import com.example.android_doan.ForgotPasswordRequest;
import com.example.android_doan.ForgotPasswordResponse;
import com.example.android_doan.LoginRequest;
import com.example.android_doan.LoginResponse;
import com.example.android_doan.ResetPasswordRequest;
import com.example.android_doan.ResetPasswordResponse;
import com.example.android_doan.model.Drink;
import com.example.android_doan.model.Salad;
import com.example.android_doan.model.SideDish;
import com.example.android_doan.model.Topping;
import com.example.android_doan.model.User;
import com.example.android_doan.ui.profile.LogoutResponse;
import com.example.android_doan.RegisterRequest;
import com.example.android_doan.RegisterResponse;
import com.example.android_doan.model.Pizza;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
    @GET("api/side/getAllSide")
    Call<List<SideDish>> getAllSide();
    @GET("api/salad/getAllSalad")
    Call<List<Salad>> getAllSalad();

    @POST("api/pizza/createPizza")
    Call<Pizza> createPizza(@Body Pizza request);

    @GET("/api/topping/getAllTopping")
    Call<List<Topping>> getAllToppings();


    @POST("api/user/forget-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    //@POST("api/user/verify-code")
    //Call<ForgotPasswordResponse> verifyCode(@Body String code);

    @POST("api/user/reset-password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);


}