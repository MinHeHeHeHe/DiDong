package com.example.android_doan.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cart {
    private String userId;
    private List<CartPizza> pizzas;
    private List<CartDrink> drinks;
    private List<CartSide> sides;
    private List<CartSalad> salads;
    private double total_price;

    public String getUserId() {
        return userId;
    }

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

    public double getTotal_price() {
        return total_price;
    }

    // === Inner classes ===

    public static class CartPizza {
        private Pizza pizzaId;
        private CustomPizza customPizza;
        private int quantity;


        public Pizza getPizzaId() {
            return pizzaId;
        }

        public CustomPizza getCustomPizza() {
            return customPizza;
        }


        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class CartDrink {
        private Drink drinkId;
        private int quantity;

        public Drink getDrinkId() {
            return drinkId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class CartSide {
        private SideDish sideId;
        private int quantity;

        public SideDish getSideId() {
            return sideId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class CartSalad {
        private Salad saladId;
        private int quantity;

        public Salad getSaladId() {
            return saladId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class CustomPizza {

        @SerializedName("pizzaId")
        private String customPizzaId;
        private String name;
        private String description;
        private String size;
        private String crust_type;
        private List<Topping> toppings;
        private double base_price;
        private String imageUrl;

        public String getCustomPizzaId() {
            return customPizzaId;
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

        public String getCrust_type() {
            return crust_type;
        }

        public List<Topping> getToppings() {
            return toppings;
        }

        public double getBase_price() {
            return base_price;
        }
        public String getImageUrl() {
            return imageUrl;
        }
    }
}
