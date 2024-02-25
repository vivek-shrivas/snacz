package com.example.snacz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {

    private List<Item> itemList;
    private List<Item> itemListFull; // A copy of itemList for filtering

    // Constructor
    public SearchAdapter(List<Item> itemList) {
        this.itemList = itemList;
        this.itemListFull = new ArrayList<>(itemList); // Initialize itemListFull with a copy of itemList
    }

    // ViewHolder class definition
    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder attributes
        private ImageView itemImageView;
        private TextView itemNameTextView;
        private TextView itemDescTextView;
        private TextView itemPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize your views here
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescTextView = itemView.findViewById(R.id.itemDescTextView);
            itemPriceTextView = itemView.findViewById(R.id.price);
        }

        // Method to bind data to views
        // Method to bind data to views
        public void bind(Item item) {
            // Initialize views
            ImageView itemImageView = itemView.findViewById(R.id.itemImageView);
            TextView foodNameTextView = itemView.findViewById(R.id.foodName);
            TextView foodDescTextView = itemView.findViewById(R.id.foodDesc);
            TextView priceTextView = itemView.findViewById(R.id.price);

            // Bind data to views
            Glide.with(itemView.getContext()).load(item.getImage()).into(itemImageView);
            foodNameTextView.setText(item.getItemName());
            foodDescTextView.setText(item.getItemDesc());
            priceTextView.setText(String.valueOf(item.getPrice()));
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem = itemList.get(position);
        holder.bind(currentItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Item> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // If the search query is empty, show all items
                filteredList.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                // Filter the itemListFull to match the constraint
                for (Item item : itemListFull) {
                    if (item.getItemName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List) results.values);
            notifyDataSetChanged();

            // Print something to log after updating the RecyclerView with filtered results
            Log.d("SearchAdapter", "Filtered items: " + itemList.size());
        }
    };

    // Method to update the itemList
    public void updateItemList(List<Item> itemList) {
        // Check if the itemList is empty or null
        if (itemList == null || itemList.isEmpty()) {
            // Clear the itemList and itemListFull to hide RecyclerView items
            this.itemList.clear();
            this.itemListFull.clear();
        } else {
            // Update itemList and itemListFull with the new data
            this.itemList = itemList;
            this.itemListFull = new ArrayList<>(itemList);
        }
        // Notify adapter of data change
        notifyDataSetChanged();
    }

}
