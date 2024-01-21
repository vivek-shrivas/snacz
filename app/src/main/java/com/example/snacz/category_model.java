package com.example.snacz;

public class category_model {
    private String category_name;
    private String category_image;
    private item_model itemModel;

    //Default constructor
    public category_model(){}

    //parameterized constructor
    public category_model(String category_name, String category_image, item_model itemModel) {
        this.category_name = category_name;
        this.category_image = category_image;
        this.itemModel = itemModel;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public item_model getItemModel() {
        return itemModel;
    }

    public void setItemModel(item_model itemModel) {
        this.itemModel = itemModel;
    }

    private static class item_model{
        private String itemName;
        private String itemDesc;
        private int itemPrice;
        private String categoryName;

        public item_model(String itemName, String itemDesc, int itemPrice ,String categoryName) {
            this.itemName = itemName;
            this.itemDesc = itemDesc;
            this.itemPrice = itemPrice;
            this.categoryName=categoryName;
        }


    }
}


