package com.example.snacz;

public class User {
    private String userId;
    private String username;
    private String email;
    private String phoneNumber;
    private Cart cart;


    //Default constructor
    public User(){}

    public User(String userId,String username, String email, String phoneNumber) {
        this.userId=userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cart = new Cart();
    }

    // Getter and setter methods for user information
    // ...

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    // Additional methods as needed
}

