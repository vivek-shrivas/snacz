package com.example.snacz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
                }
                else if (id==R.id.menu) {
                    loadFrag(new menu(),false);
                }
                else if (id==R.id.search){
                    loadFrag(new search(),false);
                }
                return true;
            }
        });

        // bottom navigation item default selection
        bnView.setSelectedItemId(R.id.home);

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
}