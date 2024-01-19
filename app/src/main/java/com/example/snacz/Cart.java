package com.example.snacz;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String userId;
    private List<menu_item_model> cartItems;


    public Cart(String userId, List<menu_item_model> cartItems) {
        this.userId = userId;
        this.cartItems = cartItems;
    }

    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    public List<menu_item_model> getCartItems() {
        return cartItems;
    }

    public void addToCart(menu_item_model item) {
        cartItems.add(item);
    }

    public void removeFromCart(menu_item_model item) {
        cartItems.remove(item);
    }

    // Methods to interact with Firebase Realtime Database
    public void saveToCart(String userId) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("cart");
        for (menu_item_model item : cartItems) {
            cartRef.child(item.getFoodName()).setValue(item);
        }
    }
}
