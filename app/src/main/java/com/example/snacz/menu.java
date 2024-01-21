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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class menu extends Fragment {
    private List<menu_item_model> menuItems;
    private menu_item_adapter menuAdapter; // Declare the adapter as a class member

    public menu() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuItems = new ArrayList<>();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialize RecyclerView for menu cards
        RecyclerView menuRecyclerView = rootView.findViewById(R.id.menu_card_recycler);
        menuAdapter = new menu_item_adapter(menuItems); // Initialize the adapter
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        menuRecyclerView.setAdapter(menuAdapter);

        // Initialize RecyclerView for sidebar
        RecyclerView sidebarRecyclerView = rootView.findViewById(R.id.menu_sidebar);

        // Create sidebar items

        List<SidebarItem> sidebarItems = new ArrayList<>();
        sidebarItems.add(new SidebarItem(R.drawable.festive_sharing, "Festive sharing"));
        sidebarItems.add(new SidebarItem(R.drawable.burger_wraps, "Burger & wraps"));
        sidebarItems.add(new SidebarItem(R.drawable.meals, "Meals"));
        sidebarItems.add(new SidebarItem(R.drawable.deserts, "Deserts"));
        sidebarItems.add(new SidebarItem(R.drawable.fries_sides, "Fries"));
        sidebarItems.add(new SidebarItem(R.drawable.saving_combos, "Saving combos"));
        // Add more sidebar items as needed
        // Set up the RecyclerView and adapter for the sidebar
        SidebarAdapter sidebarAdapter = new SidebarAdapter(requireContext(), sidebarItems);
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
                menuItems.clear(); // Assuming menuItems is your list where you store menu items

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    menu_item_model menuItem = snapshot.getValue(menu_item_model.class);
                    menuItems.add(menuItem);
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
