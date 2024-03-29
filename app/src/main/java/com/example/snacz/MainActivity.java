package com.example.snacz;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);


        bnView = findViewById(R.id.bnView);

        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    loadFrag(new home(), true);
                } else if (id == R.id.menu) {
                    loadFrag(new menu(), false);
                } else if (id == R.id.search) {
                    loadFrag(new search(), false);
                } else if (id == R.id.account) {
                    loadFrag(new Account(), false);
                }
                else if(id==R.id.cart){
                    Order order=Order.getInstance();
                    if (order.hasItems()){
                        Intent intent =new Intent(getApplicationContext(), Cart.class);
                        startActivity(intent);
                    }
                    else{
                        Toast toast=Toast.makeText(getApplicationContext(),"Your Cart is empty",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                // Clear color filter for all items
                for (int i = 0; i < bnView.getMenu().size(); i++) {
                    MenuItem menuItem = bnView.getMenu().getItem(i);
                    Objects.requireNonNull(menuItem.getIcon()).setColorFilter(null);
                }

                // Set color filter for the selected item
                Objects.requireNonNull(item.getIcon()).setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.selected_color), PorterDuff.Mode.SRC_IN);

                return true;
            }
        });

        // bottom navigation item default selection
        bnView.setSelectedItemId(R.id.home);
        bnView.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));


    }

    public FrameLayout getViewCartFrameLayoutMenu() {
        return findViewById(R.id.viewCartFrameLayoutMenu);
    }

    public TextView getViewCartQuantityTextView() {
        return findViewById(R.id.viewCartItemQuantity);
    }

    public TextView getViewCartPriceTextView(){
        return findViewById(R.id.CartTotal);
    }
    public void loadFrag(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
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
