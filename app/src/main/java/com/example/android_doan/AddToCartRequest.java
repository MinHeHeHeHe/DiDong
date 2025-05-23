package com.example.android_doan;

import java.util.List;

public class AddToCartRequest {
    private PizzaData pizza;
    private List<DrinkData> drinks;
    private List<SideData> sides;
    private List<SaladData> salads;

    public AddToCartRequest(PizzaData pizza, List<DrinkData> drinks, List<SideData> sides, List<SaladData> salads) {
        this.pizza = pizza;
        this.drinks = drinks;
        this.sides = sides;
        this.salads = salads;
    }
    public void setDrinks(List<DrinkData> drinks) {
        this.drinks = drinks;
    }
    public void setSides(List<SideData> sides) {
        this.sides = sides;
    }

    public void setSalads(List<SaladData> salads) {
        this.salads = salads;
    }


    public static class PizzaData {
        private String pizzaId; // optional
        private CustomPizza customPizza; // optional
        private int quantity;

        public PizzaData(String pizzaId, int quantity) {
            this.pizzaId = pizzaId;
            this.quantity = quantity;
        }

        public PizzaData(CustomPizza customPizza, int quantity) {
            this.customPizza = customPizza;
            this.quantity = quantity;
        }
    }

    public static class CustomPizza {
        private String pizzaId;
        private String size;
        private String crust_type;
        private List<String> toppings;
        private int quantity;

        public CustomPizza(String pizzaId, String size, String crust_type, List<String> toppings, int quantity) {
            this.pizzaId = pizzaId;
            this.size = size;
            this.crust_type = crust_type;
            this.toppings = toppings;
            this.quantity = quantity;
        }
    }

    public static class DrinkData {
        private String drinkId;
        private int quantity;

        public DrinkData(String drinkId, int quantity) {
            this.drinkId = drinkId;
            this.quantity = quantity;
        }

    }

    public static class SideData {
        private String sideId;
        private int quantity;

        public SideData(String sideId, int quantity) {
            this.sideId = sideId;
            this.quantity = quantity;
        }
    }

    public static class SaladData {
        private String saladId;
        private int quantity;

        public SaladData(String saladId, int quantity) {
            this.saladId = saladId;
            this.quantity = quantity;
        }
    }
}
