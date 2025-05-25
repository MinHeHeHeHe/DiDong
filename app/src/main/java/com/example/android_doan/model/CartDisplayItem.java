package com.example.android_doan.model;

public class CartDisplayItem {
    public enum Type { PIZZA, DRINK, SIDE, SALAD }

    private Type type;
    private Object item;
    private int mongoIndex;

    public CartDisplayItem(Type type, Object item, int mongoIndex) {
        this.type = type;
        this.item = item;
        this.mongoIndex = mongoIndex;
    }

    public Type getType() {
        return type;
    }

    public Object getItem() {
        return item;
    }

    public int getMongoIndex() {
        return mongoIndex;
    }
}
