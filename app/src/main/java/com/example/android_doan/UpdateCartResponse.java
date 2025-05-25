package com.example.android_doan;

import com.example.android_doan.model.Cart;
import com.google.gson.annotations.SerializedName;

public class UpdateCartResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("cart")
    private Cart cart;

    public String getMessage() {
        return message;
    }

    public Cart getCart() {
        return cart;
    }
}
