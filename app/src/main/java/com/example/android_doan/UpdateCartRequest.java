package com.example.android_doan;

public class UpdateCartRequest {
    private String itemType; // "pizza", "drink", etc.
    private int itemIndex;
    private int quantity;

    public UpdateCartRequest(String itemType, int itemIndex, int quantity) {
        this.itemType = itemType;
        this.itemIndex = itemIndex;
        this.quantity = quantity;
    }
}
