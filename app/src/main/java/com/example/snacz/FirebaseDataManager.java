package com.example.snacz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDataManager {
    private static final String CATEGORIES_NODE = "categories";

    private FirebaseDatabase database;
    private DatabaseReference categoriesRef;

    public FirebaseDataManager() {
        database = FirebaseDatabase.getInstance();
        categoriesRef = database.getReference(CATEGORIES_NODE);
    }

    public interface DataLoadListener {
        void onDataLoaded(List<Category> categories);

        void onDataLoadError(String errorMessage);
    }

    public void loadCategories(DataLoadListener listener) {
        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Category> categories = new ArrayList<>();

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    categories.add(category);
                }

                if (listener != null) {
                    listener.onDataLoaded(categories);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onDataLoadError(databaseError.getMessage());
                }
            }
        });
    }

    public interface DataWriteListener {
        void onDataWriteSuccess();

        void onDataWriteError(String errorMessage);
    }

    public void addCategory(Category category, DataWriteListener listener) {
        String categoryId = categoriesRef.push().getKey();
        if (categoryId != null) {
            categoriesRef.child(categoryId).setValue(category, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    if (listener != null) {
                        listener.onDataWriteSuccess();
                    }
                } else {
                    if (listener != null) {
                        listener.onDataWriteError(databaseError.getMessage());
                    }
                }
            });
        }
    }

    // Add more methods as needed for updating or deleting data

    // Example usage:
    // FirebaseDataManager dataManager = new FirebaseDataManager();
    // dataManager.loadCategories(new DataLoadListener() {
    //     @Override
    //     public void onDataLoaded(List<Category> categories) {
    //         // Handle loaded data
    //     }
    //
    //     @Override
    //     public void onDataLoadError(String errorMessage) {
    //         // Handle data load error
    //     }
    // });
}

