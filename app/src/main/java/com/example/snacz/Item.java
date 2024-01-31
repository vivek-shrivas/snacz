package com.example.snacz;
public class Item {
    private String itemName;
    private String itemDesc;

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    private String itemImage; // Assuming the image is represented by a URL
    private int price;

    public Item() {
        // Default constructor required for Firebase
    }

    public Item(String itemName, String itemDesc, String itemImage, int price) {
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getImage() {
        return itemImage;
    }

    public void setImage(String image) {
        this.itemImage = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
