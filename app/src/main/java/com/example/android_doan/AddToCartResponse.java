package com.example.android_doan;

import com.example.android_doan.model.Pizza;
import com.example.android_doan.model.Drink;
import com.example.android_doan.model.SideDish;
import com.example.android_doan.model.Salad;

import java.util.List;

public class AddToCartResponse {
    private String message;
    private Cart cart;

    public String getMessage() {
        return message;
    }

    public Cart getCart() {
        return cart;
    }

    public static class Cart {
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
    }

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
    }

    public static class CartSide {
        private SideDish sideDishId;
        private int quantity;

        public SideDish getSideId() {
            return sideDishId;
        }

        public int getQuantity() {
            return quantity;
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
    }

    public static class CustomPizza {
        private String name;
        private String description;
        private String size;
        private String crust_type;
        private List<String> toppings;
        private double base_price;

        // Getters...
    }
}
