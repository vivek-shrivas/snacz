package com.example.snacz;

public class menu_item_model {
    private String foodName;
    private String foodDesc;
    private String price;

    public static void clear() {
    }

    public static void add(menu_item_model menuItem) {
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }
    public menu_item_model(){}

    public menu_item_model(String foodName, String foodDesc, String price) {
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
}
