package com.example.android_doan.network;

import java.util.List;

import com.example.android_doan.AddToCartRequest;
import com.example.android_doan.ForgotPasswordRequest;
import com.example.android_doan.ForgotPasswordResponse;
import com.example.android_doan.LoginRequest;
import com.example.android_doan.LoginResponse;
import com.example.android_doan.ResetPasswordRequest;
import com.example.android_doan.ResetPasswordResponse;
import com.example.android_doan.UpdateCartRequest;
import com.example.android_doan.UpdateCartResponse;
import com.example.android_doan.model.Cart;
import com.example.android_doan.model.Drink;
import com.example.android_doan.model.Salad;
import com.example.android_doan.model.SideDish;
import com.example.android_doan.model.Topping;
import com.example.android_doan.ThemThongTinCaNhanRequest;
import com.example.android_doan.ThemThongTinCaNhanResponse;
import com.example.android_doan.VerifyCodeRequest;
import com.example.android_doan.VerifyCodeResponse;
import com.example.android_doan.ui.profile.LogoutResponse;
import com.example.android_doan.RegisterRequest;
import com.example.android_doan.RegisterResponse;
import com.example.android_doan.model.Pizza;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/user/getUser/{id}")
    Call<LoginResponse.User> getUserById(
            @Header("Authorization") String token,
            @Path("id") String userId
    );

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

    @PUT("api/pizza/updatePizza/{id}")
    Call<Pizza> updatePizza(@Path("id") String id, @Body Pizza pizza);


    @POST("/api/cart/addToCart")
    Call<ResponseBody> addToCart(
            @Header("Authorization") String token,
            @Body AddToCartRequest request
    );
    @POST("/api/cart/updateCartItemQuantity")
    Call<UpdateCartResponse> updateCartItemQuantity(
            @Header("Authorization") String token,
            @Body UpdateCartRequest request
    );

    @GET("/api/cart/getCart")
    Call<Cart> getCart(@Header("Authorization") String token);

    // cho user khi thêm thông tin cá nhân
    @PUT("api/user/updateUser/{id}")
    Call<ThemThongTinCaNhanResponse> updateUser(
            @Header("Authorization") String token,
            @Path("id") String userId,
            @Body ThemThongTinCaNhanRequest request
    );

    @Multipart
    @PUT("api/user/updateUser/{id}")
    Call<ThemThongTinCaNhanResponse> updateUserAvatar(
            @Header("Authorization") String token,
            @Path("id") String userId,
            @Part MultipartBody.Part image
    );

    @POST("api/user/forget-password")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest request);

    //@POST("api/user/verify-code")
    //Call<ForgotPasswordResponse> verifyCode(@Body String code);

    @POST("api/user/reset-password")
    Call<ResetPasswordResponse> resetPassword(@Body ResetPasswordRequest request);


}