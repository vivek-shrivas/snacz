package com.example.snacz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
        menuItems = new ArrayList<>(); // Initialize the class member variable
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialize RecyclerView and adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.menu_card_recycler);
        menuAdapter = new menu_item_adapter(menuItems); // Initialize the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(menuAdapter);

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
