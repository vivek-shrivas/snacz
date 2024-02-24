package com.example.snacz;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllItemsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        // Retrieve the selected category from the intent
        String selectedCategoryId = getIntent().getStringExtra("categories");

        // Initialize RecyclerView
        RecyclerView allItemsRecyclerView = findViewById(R.id.allItemsRecyclerView);
        allItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the selected category name in categoryNameTextView
        TextView categoryNameTextView = findViewById(R.id.categoryNameTextView);
        categoryNameTextView.setText(selectedCategoryId);

        TextView categoryIDTextView = findViewById(R.id.categoryIDTextView);
        categoryIDTextView.setText(selectedCategoryId);

        // Fetch items for the selected category from Firebase
        fetchItemsForCategoryFromFirebase(selectedCategoryId, allItemsRecyclerView);
    }

    private void fetchItemsForCategoryFromFirebase(String selectedCategory, RecyclerView recyclerView) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("categories");

        databaseReference.child(selectedCategory).child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> categoryItems = new ArrayList<>();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Item item = itemSnapshot.getValue(Item.class);
                    if (item != null) {
                        categoryItems.add(item);
                    }
                }

                // Set up the adapter with items related to the selected category
                ItemAdapter itemAdapter = new ItemAdapter(categoryItems);
                recyclerView.setAdapter(itemAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e("Firebase", "Error fetching items: " + databaseError.getMessage());
            }
        });
    }
}
