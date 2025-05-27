package com.example.android_doan.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Delivery {

    @SerializedName("_id")
    private String id;

    @SerializedName("userId")
    private String userId;

    @SerializedName("order")
    private Order order;

    @SerializedName("address")
    private String address;

    @SerializedName("status")
    private String status;

    @SerializedName("tracking_url")
    private String trackingUrl;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("deliveredAt")
    private String deliveredAt;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Order getOrder() {
        return order;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getDeliveredAt() {
        return deliveredAt;
    }

    // ---------- Inner classes ---------- //

    public static class Order {
        @SerializedName("pizzas")
        private List<CartPizza> pizzas;

        @SerializedName("drinks")
        private List<CartDrink> drinks;

        @SerializedName("sides")
        private List<CartSide> sides;

        @SerializedName("salads")
        private List<CartSalad> salads;

        @SerializedName("total_price")
        private double totalPrice;

        public List<CartPizza> getPizzas() {
            return pizzas;
        }

        public List<CartDrink> getDrinks() {
            return drinks;
        }

        public List<CartSide> getSides() {
            return sides;
        }

        public List<CartSalad> getSalads() {
            return salads;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }

    public static class CartPizza {
        @SerializedName("pizzaId")
        private Pizza pizza;

        @SerializedName("customPizza")
        private CustomPizza customPizza;

        @SerializedName("quantity")
        private int quantity;

        public Pizza getPizza() {
            return pizza;
        }

        public CustomPizza getCustomPizza() {
            return customPizza;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class CustomPizza {
        @SerializedName("pizzaId")
        private String pizzaId;

        @SerializedName("name")
        private String name;

        @SerializedName("description")
        private String description;

        @SerializedName("size")
        private String size;

        @SerializedName("crust_type")
        private String crustType;

        @SerializedName("toppings")
        private List<String> toppings;

        @SerializedName("base_price")
        private double basePrice;

        @SerializedName("image_url")
        private String imageUrl;

        public String getPizzaId() {
            return pizzaId;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getSize() {
            return size;
        }

        public String getCrustType() {
            return crustType;
        }

        public List<String> getToppings() {
            return toppings;
        }

        public double getBasePrice() {
            return basePrice;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class CartDrink {
        @SerializedName("drinkId")
        private Drink drinkId;

        @SerializedName("quantity")
        private int quantity;

        public Drink getDrinkId() {
            return drinkId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class CartSide {
        @SerializedName("sideId")
        private SideDish sideId;

        @SerializedName("quantity")
        private int quantity;

        public SideDish getSideId() {
            return sideId;
        }

        public int getQuantity() {
            return quantity;
        }
    }

    public static class CartSalad {
        @SerializedName("saladId")
        private Salad saladId;

        @SerializedName("quantity")
        private int quantity;

        public Salad getSaladId() {
            return saladId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}