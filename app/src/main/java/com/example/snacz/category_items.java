package com.example.snacz;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.snacz.Item;
import com.example.snacz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class category_items extends Fragment {
    private RecyclerView recyclerView;
    private TextView categoryNameTextView;
    private String selectedCategory;
    private List<Item> itemList;
    private CategoryItemsAdapter adapter;

    public category_items() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
        if (getArguments() != null) {
            selectedCategory = getArguments().getString("category");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_items, container, false);

        // Initialize views
        categoryNameTextView = view.findViewById(R.id.categoryNameTextView);
        recyclerView = view.findViewById(R.id.categoryItemsRecycler);

        // Set category name
        categoryNameTextView.setText(selectedCategory);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CategoryItemsAdapter(itemList, getContext());
        recyclerView.setAdapter(adapter);

        // Fetch items for the selected category
        fetchItems(selectedCategory);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter = new CategoryItemsAdapter(itemList, getContext());
        recyclerView.setAdapter(adapter);
    }

    public void setSelectedCategory(String category) {
        selectedCategory = category;
        // Update the category name when a new category is selected
        if (categoryNameTextView != null) {
            categoryNameTextView.setText(selectedCategory);
        }

        Log.d("Selected category","selected category is "+selectedCategory);
        // Fetch items for the newly selected category
        fetchItems(selectedCategory);
    }
    private void fetchItems(String categoryName) {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        // Query to find the category node by its categoryName
        Query categoryQuery = categoriesRef.orderByChild("categoryName").equalTo(categoryName);

        categoryQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    // Once we find the category node with the matching categoryName, fetch its items
                    DataSnapshot itemsSnapshot = categorySnapshot.child("items");
                    itemList.clear();
                    for (DataSnapshot snapshot : itemsSnapshot.getChildren()) {
                        Long item_id = snapshot.child("item_id").exists() ? snapshot.child("item_id").getValue(Long.class) : null;
                        String itemName = snapshot.child("itemName").getValue(String.class);
                        String itemDesc = snapshot.child("itemDesc").getValue(String.class);
                        String itemImage = snapshot.child("image").getValue(String.class);
                        Integer price = snapshot.child("price").exists() ? snapshot.child("price").getValue(Integer.class) : null;
                        if (item_id != null && itemName != null && itemDesc != null && itemImage != null && price != null) {
                            Item item = new Item(item_id, itemName, itemDesc, itemImage, price);
                            itemList.add(item);
                        } else {
                            // Handle the case where any of the required fields is null
                            Log.e("DataParsing", "Skipping item due to missing or null values");
                        }
                    }
                }
                adapter.notifyDataSetChanged();

            // Handle the case when no category with the given categoryName is found
                Log.d(TAG, "No category found with name: " + categoryName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e(TAG, "Error fetching categories: " + databaseError.getMessage());
            }
        });
    }

}
