package com.example.android_doan;

import com.example.android_doan.model.Delivery;
import com.google.gson.annotations.SerializedName;

public class DeliveryResponse {
    @SerializedName("delivery")
    private Delivery delivery;

    @SerializedName("message")
    private String message;

    public Delivery getDelivery() {
        return delivery;
    }

    public String getMessage() {
        return message;
    }
}
