package com.example.snacz;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
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

public class search extends Fragment {

    private RecyclerView searchRecycler;
    private SearchAdapter searchAdapter;
    private List<Item> itemList = new ArrayList<>();
    private SearchView searchBar;
    private DatabaseReference databaseReference;

    public search() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize RecyclerView
        searchRecycler = rootView.findViewById(R.id.searchRecycler);
        searchRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize SearchAdapter with the complete itemList
        searchAdapter = new SearchAdapter(itemList);
        searchRecycler.setAdapter(searchAdapter);

        // Initialize SearchView
        searchBar = rootView.findViewById(R.id.searchBar);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("categories");

        // Fetch all items from Firebase and populate RecyclerView
        fetchAllItemsFromFirebase();

        // Set up search functionality when user submits query
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when user submits query
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: Perform search while user is typing
                // performSearch(newText);
                return true;
            }
        });

        return rootView;
    }


    private void fetchAllItemsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot itemsSnapshot = categorySnapshot.child("items");
                    for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                        // Fetch item details
                        String itemName = itemSnapshot.child("itemName").getValue(String.class);
                        String itemDesc = itemSnapshot.child("itemDesc").getValue(String.class);
                        String itemImage = itemSnapshot.child("image").getValue(String.class);
                        int itemPrice = itemSnapshot.child("price").getValue(Integer.class);
                        Long itemId = itemSnapshot.child("item_id").getValue(Long.class);
                        // Create Item object
                        Item item = new Item(itemId, itemName, itemDesc, itemImage, itemPrice);
                        // Add item to the list
                        itemList.add(item);
                    }
                }
                // Update RecyclerView adapter with fetched items
                searchAdapter.updateItemList(itemList);
                Log.d(TAG, "Fetched items: " + itemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to fetch items: " + databaseError.getMessage());
            }
        });
    }


    private void performSearch(String query) {
        // Implement your search logic here
        // For example, filter itemList based on the query and update the RecyclerView
        List<Item> filteredList = new ArrayList<>();
        for (Item item : itemList) {
            if (item.getItemName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        searchAdapter.updateItemList(filteredList);
    }
}
