package com.example.snacz;


import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Account extends Fragment {

    private RecyclerView orderHistoryRecycler;
    private List<Map<String, Object>> orderList;
    private AccountAdapter adapter;
    private String userNameString,phoneNumberString;
    private TextView userName;
    private TextView phoneNumber;

    private ImageView logoutImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        orderHistoryRecycler = view.findViewById(R.id.orderHistoryRecycler);
        orderHistoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        adapter = new AccountAdapter(orderList);
        orderHistoryRecycler.setAdapter(adapter);
        userName=view.findViewById(R.id.userName);
        phoneNumber=view.findViewById(R.id.mobileNumber);
        logoutImageView=view.findViewById(R.id.logout);

        userNameString=SharedPreferencesManager.getInstance(getContext()).getUsername();
        phoneNumberString=SharedPreferencesManager.getInstance(getContext()).getPhoneNumber();
        userName.setText(userNameString);
        phoneNumber.setText(phoneNumberString);
        fetchUserOrders();

        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.getInstance(getContext()).clearAllFields();
                Intent intent= new Intent(getContext(), PhoneNumberEntryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }






    private void fetchUserOrders() {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(requireContext());
        String userId = sharedPreferencesManager.getAuthToken();

        DatabaseReference userOrdersRef = FirebaseDatabase.getInstance().getReference().child("orders").child("delivery").child(userId);
        userOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot userOrderSnapshot : dataSnapshot.getChildren()) {
                    // Get the userId (which is also the orderId in this case)
                    String orderId = userOrderSnapshot.getKey();

                    // Get the order data
                    Map<String, Object> orderData = (Map<String, Object>) userOrderSnapshot.getValue();

                    // Add the orderId as a key-value pair in the orderData map
                    orderData.put("orderId", orderId);

                    // Add the orderData to the list
                    orderList.add(orderData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}

