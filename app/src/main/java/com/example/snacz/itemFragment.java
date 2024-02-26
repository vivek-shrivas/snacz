package com.example.snacz;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// ... (existing imports)

public class itemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);

        // Retrieve category ID and name from arguments
        String categoryId = getArguments().getString("categories");
        String categoryName = getArguments().getString("categoryName");

        // Set category name in TextView
        TextView categoryNameTextView = rootView.findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(categoryName);

        // Initialize RecyclerView
        RecyclerView itemRecyclerView = rootView.findViewById(R.id.allItemsRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch items related to the category and set up the adapter
        fetchAllItemsFromFirebase(itemRecyclerView);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        ItemAdapter itemAdapter;
        List<Item> itemList = Order.getInstance().getItems();
         itemAdapter = new ItemAdapter(itemList);
            itemAdapter.setData(itemList);
    }

    private void fetchAllItemsFromFirebase(RecyclerView recyclerView) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("categories");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> allItems = new ArrayList<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : categorySnapshot.child("items").getChildren()) {
                        Item item = itemSnapshot.getValue(Item.class);
                        allItems.add(item);
                    }
                }

                // Set up the adapter with all items
                ItemAdapter itemAdapter = new ItemAdapter(allItems);
                recyclerView.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void setupRecyclerViewAdapter(RecyclerView recyclerView, List<Item> items) {
        ItemAdapter itemAdapter = new ItemAdapter(items);
        recyclerView.setAdapter(itemAdapter);
    }
}
