package com.example.snacz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Cart extends AppCompatActivity {

    TextView totalPayable;
    TextView subTotal;
    TextView CGST;
    TextView SGST;
    Button placeOrderButton;
    RecyclerView cartRecyclerView;

    CartAdapter cartAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        totalPayable = findViewById(R.id.totalPayableAmount);
        subTotal = findViewById(R.id.totalAmount);
        CGST = findViewById(R.id.CGST);
        SGST = findViewById(R.id.SGST);

        // Initialize views
        cartRecyclerView = findViewById(R.id.cartRecycler);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        cartRecyclerView.setLayoutManager(layoutManager);

        // Get items from the Order singleton instance
        List<Item> itemList = Order.getInstance().getItems();

        // Initialize and set the adapter
        cartAdapter = new CartAdapter(itemList);
        cartRecyclerView.setAdapter(cartAdapter);

        calculateGSTAndDisplay();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Update adapter data when the activity resumes
        updateData(Order.getInstance().getItems());
    }

    private void updateData(List<Item> itemList) {
        if (cartAdapter != null) {
            cartAdapter.setData(itemList);
            cartAdapter.notifyDataSetChanged();
        }
    }

    private void calculateGSTAndDisplay() {
        // Get total amount from Order class
        double totalAmount = Order.getInstance().calculateTotal();

        // Calculate CGST and SGST using Order class method
        Order.getInstance().calculateGST(totalAmount);

        // Display total amount, CGST, and SGST in respective TextViews
        subTotal.setText(String.format("%.2f", totalAmount));
        CGST.setText(String.format("%.2f", Order.getInstance().CGST));
        SGST.setText(String.format("%.2f", Order.getInstance().SGST));

        // Calculate total payable amount (Total + CGST + SGST)
        double totalPayableAmount = totalAmount + Order.getInstance().CGST + Order.getInstance().SGST;
        totalPayable.setText(String.format("%.2f", totalPayableAmount));
    }
}
