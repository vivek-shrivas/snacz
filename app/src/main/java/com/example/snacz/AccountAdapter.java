package com.example.snacz;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private List<Map<String, Object>> orderList;

    public AccountAdapter(List<Map<String, Object>> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String, Object> orderData = orderList.get(position);

        // Check if orderData is null or not a Map
        if (orderData != null && orderData instanceof Map) {
            // Use the orderId retrieved from orderData
            String orderId = (String) orderData.get("orderId");
            holder.orderIdTextView.setText("Order Id :"+orderId);

            // Store the orderId in a tag for later retrieval if needed
            holder.itemView.setTag(orderId);

            holder.orderStatusTextView.setText(String.valueOf(orderData.get("status")));
            holder.orderPriceTextView.setText("₹" + String.valueOf(orderData.get("total")));

            // Check if "items" key exists and is of type List
            if (orderData.containsKey("items") && orderData.get("items") instanceof List) {
                List<Map<String, Object>> itemsList = (List<Map<String, Object>>) orderData.get("items");

                // Set the number of items in the order
                holder.itemQuantityTextView.setText(String.valueOf(itemsList.size()+" Items"));

                // Construct the string for ordered items
                StringBuilder orderedItemsBuilder = new StringBuilder();
                for (Map<String, Object> itemData : itemsList) {
                    String itemName = String.valueOf(itemData.get("itemName"));
                    long quantity = (long) itemData.get("itemQuantity"); // Cast to long
                    orderedItemsBuilder.append(quantity).append(" x ").append(itemName).append("\n");
                }
                holder.quantityXorderedItemNameTextView.setText(orderedItemsBuilder.toString());
            } else {
                // Handle missing or invalid "items" key
                holder.quantityXorderedItemNameTextView.setText("Items not available");
                holder.itemQuantityTextView.setText("0"); // No items
            }
        } else {
            // Handle null or unexpected data format
            holder.orderIdTextView.setText("Order ID not available");
            holder.orderStatusTextView.setText("Status not available");
            holder.orderPriceTextView.setText("Price not available");
            holder.quantityXorderedItemNameTextView.setText("Items not available");
            holder.itemQuantityTextView.setText("0"); // No items
        }
    }



    @Override
    public int getItemCount() {
        int order=orderList.size();
        Log.d("",""+order);
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView itemQuantityTextView;
        TextView quantityXorderedItemNameTextView;
        TextView orderStatusTextView;
        TextView orderPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderId);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantity);
            quantityXorderedItemNameTextView = itemView.findViewById(R.id.quantityXorderedItemNameTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatus);
            orderPriceTextView = itemView.findViewById(R.id.orderPrice);
        }
    }
}
