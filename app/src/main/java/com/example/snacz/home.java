package com.example.snacz;
import com.bumptech.glide.Glide;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class home extends Fragment {

    private ImageButton deliveryButton;
    private ImageButton takeawayButton;

    private Order currentOrder;

    private CardView menuCard1;

    private CardView menuCard2;
    private CardView menuCard3;
    private CardView menuCard4;
    private CardView menuCard5;
    private CardView menuCard6;
    private CardView menuCard7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        deliveryButton = view.findViewById(R.id.delivery_btn);
        takeawayButton = view.findViewById(R.id.takeaway_btn);
        currentOrder = new Order(Order.delivery);

        // Set the deliveryButton to be checked by default
        deliveryButton.setSelected(true);
        deliveryButton.setOnClickListener(v -> updateOrderType(String.valueOf(Order.delivery)));
        takeawayButton.setOnClickListener(v -> updateOrderType(String.valueOf(Order.takeaway)));

        deliveryButton.setSelected(true);
        // Optionally: Add logic to update UI based on the order type change
        deliveryButton.setBackgroundTintList(createColorStateList(R.color.red));
        takeawayButton.setBackgroundTintList(createColorStateList(R.color.white));

        deliveryButton.setOnClickListener(v -> {
            // Update the order type to delivery
            currentOrder.setOrderType(Order.delivery);
            // Optionally: Add logic to update UI based on the order type change
            deliveryButton.setBackgroundTintList(createColorStateList(R.color.red));

            takeawayButton.setBackgroundTintList(createColorStateList(R.color.white));
        });

        takeawayButton.setOnClickListener(v -> {
            // Update the order type to takeaway
            currentOrder.setOrderType(Order.takeaway);
            // Optionally: Add logic to update UI based on the order type change
            deliveryButton.setBackgroundTintList(createColorStateList(R.color.white));
            takeawayButton.setBackgroundTintList(createColorStateList(R.color.red));
        });

        fetchCategories();

        return view;
    }


    private void updateOrderType(String orderType) {
        currentOrder.setOrderType(Integer.parseInt(orderType));
    }

    private void fetchCategories() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("categories");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Category> categories = new ArrayList<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    categories.add(category);
                }

                updateUIWithCategories(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void updateUIWithCategories(List<Category> categories) {
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            updateCategoryUI(category, i + 1);
        }
    }

    private void updateCategoryUI(Category category, int index) {
        // Set the image in the corresponding ImageView
        String imageViewId = "menuImg" + index; // e.g., menuImg1, menuImg2, ...
        ImageView imageView = requireView().findViewById(getResources().getIdentifier(imageViewId, "id", requireContext().getPackageName()));
        String categoryImage = category.getCategoryImage();

        // Load image from URL using Glide
        Glide.with(requireContext())
                .load(categoryImage)
                .into(imageView);

        // Set the text in the corresponding TextView
        String textViewId = "menuTxt" + index; // e.g., menuTxt1, menuTxt2, ...
        TextView textView = requireView().findViewById(getResources().getIdentifier(textViewId, "id", requireContext().getPackageName()));

        if (textView != null) {
            textView.setText(category.getCategoryName());
        } else {
            Log.e("DebugTag", "TextView not found for ID: " + textViewId);
        }
    }

    private ColorStateList createColorStateList(int colorResId) {
        return ContextCompat.getColorStateList(requireContext(), colorResId);
    }
    public void loadFrag(Fragment fragment, boolean flag) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag) {
            ft.add(R.id.MainFrame, fragment);
            ft.commit();
        } else {
            ft.replace(R.id.MainFrame, fragment);
            ft.commit();
        }
    }
}
