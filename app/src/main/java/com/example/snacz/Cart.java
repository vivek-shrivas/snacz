package com.example.snacz;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String userId;
    private List<Item> cartItems;


    public Cart(String userId, List<Item> cartItems) {
        this.userId = userId;
        this.cartItems = cartItems;
    }

    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    public List<Item> getCartItems() {
        return cartItems;
    }

    public void addToCart(Item item) {
        cartItems.add(item);
    }

    public void removeFromCart(Item item) {
        cartItems.remove(item);
    }

    // Methods to interact with Firebase Realtime Database
    public void saveToCart(String userId) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
        for (Item item : cartItems) {
            cartRef.child(item.getItemName()).setValue(item);
        }
    }
}
