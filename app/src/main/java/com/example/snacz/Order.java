package com.example.snacz;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.snacz.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Order {
    public static final int delivery = 1;
    public static final int takeaway = 2;

    public int CGST;
    public int SGST;

    public static Order instance;

    private List<Item> items;
    private int orderType;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(int orderType){
        this.items=new ArrayList<>();
        this.orderType=orderType;
    }
    public static Order getInstance() {
        if (instance == null) {
            instance = new Order();
        }
        return instance;
    }
public int itemQuantity() {
            int Quantity=0;
            for (Item item : this.items) {
                Quantity+=1;
            }
            Log.d("total items in cart","items in cart ="+Quantity);
    return Quantity;

    }


    public boolean hasItems() {
        return !items.isEmpty();
    }
    public void pushOrderToFirebase(Context context, Order order) {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(context);
        String authToken = sharedPreferencesManager.getAuthToken();
        if (authToken != null) {
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
            DatabaseReference userOrderRef;

            // Get UID from SharedPreferences or any other method
            String uid = sharedPreferencesManager.getAuthToken(); // Replace this with the actual UID retrieval logic

            // Create a timestamp
            long timestamp = System.currentTimeMillis();

            // Determine the order type as a string
            String orderTypeString;
            if (order.getOrderType() == Order.delivery) {
                orderTypeString = "delivery";
            } else {
                orderTypeString = "takeaway";
            }

            // Set the order data based on order type
            userOrderRef = ordersRef.child(orderTypeString).child(uid).child(String.valueOf(timestamp));
            int total;
            total=order.calculateTotal();
            total+=order.CGST+order.SGST;
            // Create a map to hold order data including items list
            Map<String, Object> orderData = new HashMap<>();
            orderData.put("orderType", orderTypeString);
            orderData.put("total", total);
            orderData.put("items", order.getItems());
            orderData.put("status","Preparing");

            // Push order data to Firebase
            userOrderRef.setValue(orderData);

            // Clear the Order data
            order.clear();
        }
    }

    public void clear() {
        // Reset or clear all the fields in the Order object
        // For example, if you have a List<Item> items, you can clear it like this:
        items.clear();
        // You can similarly clear other fields or reset them to their initial state
    }


    public void addItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    public int getOrderType() {
        return this.orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
    public List<Item> getItems() {
        return this.items;
    }

    public int calculateTotal() {
        int total = 0;
        for (Item item : items) {
            total += item.getPrice()*item.getItemQuantity();
        }
        return total;
    }
    public void calculateGST(int totalAmount) {
        // Calculate CGST and SGST percentages
        int cgstPercentage = 9; // 9% CGST
        int sgstPercentage = 9; // 9% SGST

        // Calculate CGST and SGST amounts
        int cgstAmount = (totalAmount * cgstPercentage) / 100;
        int sgstAmount = (totalAmount * sgstPercentage) / 100;

        // Assign the calculated values to the instance variables
        this.CGST = cgstAmount;
        this.SGST = sgstAmount;

        // Print or log the calculated CGST and SGST amounts
        Log.d("GST", "CGST: " + cgstAmount);
        Log.d("GST", "SGST: " + sgstAmount);
    }
    public static int getTotalNumberOfItems() {
        return instance.items.size();
    }


}
