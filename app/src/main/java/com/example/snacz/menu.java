        package com.example.snacz;
        import static android.content.Intent.getIntent;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.Layout;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.core.content.ContextCompat;
        import androidx.core.view.MenuItemCompat;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

        public class menu extends Fragment {
            private List<Item> items;
            private SidebarAdapter sidebarAdapter;
            private String selectedCategory;
            private Button search;
            // Assuming you have a List<Category> called categoryList in your menu fragment
            private List<Category> categoryList;
            private TextView viewCart;
            FrameLayout viewCartFrameLayoutMenu;
            BottomNavigationView bnView;

            MainActivity mainActivity;

            public menu() {
                // Required empty public constructor
            }

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                items = new ArrayList<>();
                categoryList = new ArrayList<>();// Initialize the categoryList
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
                search=rootView.findViewById(R.id.search_btn_menu);
                viewCartFrameLayoutMenu = rootView.findViewById(R.id.viewCartFrameLayoutMenu);
                bnView=getActivity().findViewById(R.id.bnView);
                View viewCartView = LayoutInflater.from(getContext()).inflate(R.layout.view_cart, viewCartFrameLayoutMenu, false);
                TextView viewCartTextView = viewCartView.findViewById(R.id.viewCart);
                Order order = Order.getInstance();
                mainActivity=new MainActivity();
                // To get the first category name when the bottom navigation is clicked for the menu fragment and the first category items should get loaded automatically
                fetchFirstCategoryName();

                if (order.hasItems()) {
                    viewCartFrameLayoutMenu.setVisibility(View.VISIBLE);
                    viewCartFrameLayoutMenu.addView(viewCartView);
                    Log.d("i got here","i got here ");
                }
                else{
                    viewCartFrameLayoutMenu.setVisibility(View.INVISIBLE);
                }
                Bundle args = getArguments();
                if (args != null) {
                    String categoryName = args.getString("categoryName");
                    if (categoryName != null) {
                        onCategorySelected(categoryName);
                    }
                }
                viewCartTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Cart.class);
                        startActivity(intent);

//                        order.pushOrderToFirebase(getContext(),order);
                    }
                });
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bnView.setSelectedItemId(R.id.search);
                    }
                });

                RecyclerView sidebarRecyclerView = rootView.findViewById(R.id.menu_sidebar);

                sidebarAdapter = new SidebarAdapter(requireContext(), categoryList, this::onCategorySelected);
                sidebarRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                sidebarRecyclerView.setAdapter(sidebarAdapter);

                // Fetch categories from Firebase
                fetchCategoriesFromFirebase();

                return rootView;
            }



            private void fetchFirstCategoryName() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("categories");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                            String firstCategoryName = categorySnapshot.child("categoryName").getValue(String.class);
                            if (firstCategoryName != null) {
                                // Do something with the first category name
                                // For example, call a method passing the first category name
                               onCategorySelected(firstCategoryName);
                                break; // Exit the loop after getting the first category name
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle errors
                    }
                });
            }

            private void onCategorySelected(String category) {
                selectedCategory = category;
                // Create an instance of category_items Fragment
                category_items allItemsFragment = new category_items();

                // Attach the fragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_container_item_fragment, allItemsFragment)
                        .commit();

                // Set the selected category
                allItemsFragment.setSelectedCategory(selectedCategory);
            }

            private void fetchCategoriesFromFirebase() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("categories");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        categoryList.clear();

                        for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                            Category category = categorySnapshot.getValue(Category.class);
                            categoryList.add(category);
                        }

                        sidebarAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching categories: " + databaseError.getMessage());
                    }
                });
            }

        }
