    package com.example.snacz;
    import com.bumptech.glide.Glide;

    import android.content.Intent;
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

        BottomNavigationView bnView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            bnView=getActivity().findViewById(R.id.bnView);
            deliveryButton = view.findViewById(R.id.delivery_btn);
            takeawayButton = view.findViewById(R.id.takeaway_btn);
            Order order =Order.getInstance();
            // Set the deliveryButton to be checked by default
            deliveryButton.setSelected(true);

            if(order.getOrderType()==Order.delivery){
                order.setOrderType(Order.delivery);
                // Optionally: Add logic to update UI based on the order type change
                deliveryButton.setBackgroundTintList(createColorStateList(R.color.red));

                takeawayButton.setBackgroundTintList(createColorStateList(R.color.white));
            }
            else if(order.getOrderType()==Order.takeaway){
                order.setOrderType(Order.takeaway);
                deliveryButton.setBackgroundTintList(createColorStateList(R.color.white));
                takeawayButton.setBackgroundTintList(createColorStateList(R.color.red));
            }
            else{
                order.setOrderType(Order.delivery);
            }
            deliveryButton.setOnClickListener(v -> updateOrderType(String.valueOf(Order.delivery)));
            takeawayButton.setOnClickListener(v -> updateOrderType(String.valueOf(Order.takeaway)));

            deliveryButton.setSelected(true);
            // Optionally: Add logic to update UI based on the order type change
            deliveryButton.setBackgroundTintList(createColorStateList(R.color.red));
            takeawayButton.setBackgroundTintList(createColorStateList(R.color.white));

            deliveryButton.setOnClickListener(v -> {
                // Update the order type to delivery
                order.setOrderType(Order.delivery);
                // Optionally: Add logic to update UI based on the order type change
                deliveryButton.setBackgroundTintList(createColorStateList(R.color.red));

                takeawayButton.setBackgroundTintList(createColorStateList(R.color.white));
            });

            takeawayButton.setOnClickListener(v -> {
                order.setOrderType(Order.takeaway);
                deliveryButton.setBackgroundTintList(createColorStateList(R.color.white));
                takeawayButton.setBackgroundTintList(createColorStateList(R.color.red));
            });


            fetchCategories();

            return view;
        }

        @Override
        public void onResume(){
            super.onResume();
            Order order=Order.getInstance();
            if(order.getOrderType()==Order.delivery){
                order.setOrderType(Order.delivery);
                // Optionally: Add logic to update UI based on the order type change
                deliveryButton.setBackgroundTintList(createColorStateList(R.color.red));

                takeawayButton.setBackgroundTintList(createColorStateList(R.color.white));
            }
            else if(order.getOrderType()==Order.takeaway){
                order.setOrderType(Order.takeaway);
                deliveryButton.setBackgroundTintList(createColorStateList(R.color.white));
                takeawayButton.setBackgroundTintList(createColorStateList(R.color.red));
            }
            else{
                order.setOrderType(Order.delivery);
            }

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


        // ... (existing code)
        private void updateCategoryUI(Category category, int index) {
            View view = getView(); // Get the fragment's view

            if (view != null) {
                // Set the image in the corresponding ImageView
                String imageViewId = "menuImg" + index; // e.g., menuImg1, menuImg2, ...
                ImageView imageView = view.findViewById(getResources().getIdentifier(imageViewId, "id", requireContext().getPackageName()));
                String categoryImage = category.getCategoryImage();

                // Load image from URL using Glide
                Glide.with(requireContext())
                        .load(categoryImage)
                        .into(imageView);

                // Set the text in the corresponding TextView
                String textViewId = "menuTxt" + index; // e.g., menuTxt1, menuTxt2, ...
                TextView textView = view.findViewById(getResources().getIdentifier(textViewId, "id", requireContext().getPackageName()));

                if (textView != null) {
                    textView.setText(category.getCategoryName());

                    // Set the click listener for the menu card
                    String cardViewId = "menuCard" + index; // e.g., menuCard1, menuCard2, ...
                    CardView menuCard = view.findViewById(getResources().getIdentifier(cardViewId, "id", requireContext().getPackageName()));
                    menuCard.setOnClickListener(v -> {
                        // On menu card click, launch MenuFragment with category name as an argument
                        launchMenuFragment(category.getCategoryName());
                    });
                } else {
                    Log.e("DebugTag", "TextView not found for ID: " + textViewId);
                }
            }
        }


        private void launchMenuFragment(String categoryName) {
            // Create a new instance of MenuFragment and pass the category name as an argument
            menu menuFragment = new menu();
            Bundle bundle = new Bundle();
            bundle.putString("categoryName", categoryName);
            menuFragment.setArguments(bundle);

            // Add the menuFragment to the fragment manager
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.MainFrame, menuFragment);
            transaction.addToBackStack(null);  // Optional: Add to back stack if you want to navigate back to the previous fragment
            transaction.commit();

            // Set the selected item in the bottom navigation view
            bnView.setSelectedItemId(R.id.menu);
        }

        // ... (existing code)
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
