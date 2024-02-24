package com.example.snacz;
public class Item {

    private String categoryName;

    private int quantity;
    private String itemImage;
    private Long item_id;
    private String itemDesc;
    private String itemName;
    private int price;

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }


    public Item() {
        // Default constructor required for Firebase
    }

    public Item(Long item_id,String itemName, String itemDesc, String itemImage, int price) {
        this.item_id = item_id;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemImage = itemImage;
        this.price = price;

    }

    public Long getItemId(){
        return item_id;
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

    public int getItemQuantity(){
        return this.quantity;
    }
    public void decreaseQuantity(){
        this.quantity-=1;
    }

    public void increaseQuantity(){
        this.quantity+=1;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
