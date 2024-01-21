package com.example.snacz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnView=findViewById(R.id.bnView);
        //noinspection deprecation

        bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.home)
                {
                    loadFrag(new home(),true);
                    configureBottomNavigationTint();
                }
                else if (id==R.id.menu) {
                    loadFrag(new menu(),false);
                    configureBottomNavigationTint();
                }
                else if (id==R.id.search){
                    loadFrag(new search(),false);
                    configureBottomNavigationTint();
                }
                return true;
            }
        });

        // bottom navigation item default selection
        bnView.setSelectedItemId(R.id.home);
        configureBottomNavigationTint();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, Android");

    }
    public void loadFrag(Fragment fragment, boolean flag){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(flag){
            ft.add(R.id.MainFrame,fragment);
            ft.commit();
        }
        else {
            ft.replace(R.id.MainFrame,fragment);
            ft.commit();
        }
    }

    private void configureBottomNavigationTint() {
        // Use the selector directly as a ColorStateList
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.bottom_nav_selected_color);

        // Apply the ColorStateList to the BottomNavigationView
        bnView.setItemIconTintList(colorStateList);
    }

}