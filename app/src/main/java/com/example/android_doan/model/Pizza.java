package com.example.android_doan.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Pizza {
    @SerializedName("_id")
    private String id;
    private String name;
    private String description;
    private String size;
    @SerializedName("crust_type")
    private String crustType;
    private List<String> toppings;
    @SerializedName("base_price")
    private double basePrice;
    @SerializedName("image_url")
    private String imageUrl;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSize() { return size; }
    public String getCrustType() { return crustType; }
    public List<String> getToppings() { return toppings; }
    public double getBasePrice() { return basePrice; }

    // Setters (for createPizza)
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setSize(String size) { this.size = size; }
    public void setCrustType(String crustType) { this.crustType = crustType; }
    public void setToppings(List<String> toppings) { this.toppings = toppings; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public String getImageUrl() {
        return imageUrl;
    }

}