package com.example.android_doan;

import com.google.gson.annotations.SerializedName;

public class PaymentResponse {
    @SerializedName("paymentUrl")
    private String paymentUrl;

    @SerializedName("paymentId")
    private String paymentId;


    public String getPaymentUrl() {
        return paymentUrl;
    }

    public String getPaymentId() {
        return paymentId;
    }
}
