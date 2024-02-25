package com.example.snacz;

import android.os.Bundle;
import android.view.View;
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
        cartAdapter = new CartAdapter(itemList, this);
        cartRecyclerView.setAdapter(cartAdapter);

        calculateGSTAndDisplay();
        updateTotalPayable(); // Calculate and display total payable initially

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = Order.getInstance();
                Order.getInstance().pushOrderToFirebase(getApplicationContext(), order);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData(Order.getInstance().getItems());
        updateTotalPayable(); // Update total payable whenever activity resumes
    }

    private void updateData(List<Item> itemList) {


        if (cartAdapter != null) {
            cartAdapter.setData(itemList);
        }
    }

    private void calculateGSTAndDisplay() {
        int totalAmount = Order.getInstance().calculateTotal();

        // Calculate CGST and SGST using Order class method
        Order.getInstance().calculateGST(totalAmount);

        // Display total amount, CGST, and SGST in respective TextViews
        subTotal.setText(String.valueOf(totalAmount));
        CGST.setText(String.valueOf(Order.getInstance().CGST));
        SGST.setText(String.valueOf(Order.getInstance().SGST));

        // Calculate total payable amount (Total + CGST + SGST)
        int totalPayableAmount = totalAmount + Order.getInstance().SGST + Order.getInstance().CGST;
        totalPayable.setText(String.valueOf(totalPayableAmount));
        placeOrderButton.setText(String.valueOf(totalPayableAmount));
    }

    // Update total payable amount when called
    public void updateTotalPayable() {
        calculateGSTAndDisplay();
    }
}
