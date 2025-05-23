package com.example.android_doan.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Topping implements Serializable {
    @SerializedName("_id")
    private String id;

    private String name;
    private String description;

    @SerializedName("base_price")
    private double basePrice;

    @SerializedName("image_url")
    private String imageUrl;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getBasePrice() { return basePrice; }
    public String getImageUrl() { return imageUrl; }

    // Setters (for createTopping)
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
