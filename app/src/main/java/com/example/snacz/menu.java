package com.example.snacz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class menu extends Fragment {
    private List<Item> items;
    private menu_item_adapter menuAdapter; // Declare the adapter as a class member

    public menu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialize RecyclerView for menu cards
        RecyclerView menuRecyclerView = rootView.findViewById(R.id.menu_card_recycler);
        menuAdapter = new menu_item_adapter(items); // Initialize the adapter
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuRecyclerView.setAdapter(menuAdapter);

        // Initialize RecyclerView for sidebar
        RecyclerView sidebarRecyclerView = rootView.findViewById(R.id.menu_sidebar);

        SidebarAdapter sidebarAdapter = new SidebarAdapter(requireContext(), null);
        sidebarRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        sidebarRecyclerView.setAdapter(sidebarAdapter);

        // Fetch data from Firebase
        fetchDataFromFirebase();

        return rootView;
    }


    private void fetchDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("menu");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                
                items.clear(); // Assuming menuItems is your list where you store menu items

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Item item = snapshot.getValue(Item.class);
                    items.add(item);
                }

                // Notify the adapter that the data has changed
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
            }
        });
    }
}
