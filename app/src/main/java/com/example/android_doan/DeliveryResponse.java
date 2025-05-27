package com.example.android_doan;

import com.google.gson.annotations.SerializedName;

public class DeliveryResponse {

    @SerializedName("deliveryId")
    private String deliveryId;

    @SerializedName("trackingUrl")
    private String trackingUrl;

    @SerializedName("message")
    private String message;

    public String getDeliveryId() {
        return deliveryId;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public String getMessage() {
        return message;
    }
}
