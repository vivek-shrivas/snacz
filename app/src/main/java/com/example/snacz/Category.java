package com.example.snacz;

import java.util.List;
import java.util.Map;

public class Category {
    private String categoryName;
    private String image; // Assuming the image is represented by a URL
    private List<Item> items;

    public Category() {
        // Default constructor required for Firebase
    }


    public Category(String categoryName, String image, List<Item> items) {
        this.categoryName = categoryName;
        this.image = image;
        this.items = items;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategoryImage(String categoryImage) {
        this.image = categoryImage;
    }

    public String getImage() {
        return image;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
