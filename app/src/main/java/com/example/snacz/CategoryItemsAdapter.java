package com.example.snacz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snacz.Item;
import com.example.snacz.Order;
import com.example.snacz.R;

import java.util.List;

public class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.ViewHolder> {
    private List<Item> itemList;
    private Context context;
    private Order order;

    public CategoryItemsAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.order = Order.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.foodNameTextView.setText(item.getItemName());
        holder.foodDescTextView.setText(item.getItemDesc());
        Glide.with(holder.itemView.getContext())
                .load(item.getItemImage())
                .into(holder.itemImageView);
        holder.priceTextView.setText(String.valueOf("₹" + item.getPrice()));

        // Check if the item is already in the order
        if (isItemInOrder(item)) {
            // If the item is in the order, hide the addBtn and show the itemBtnIncreaseQtyDecreaseQtyholder
            holder.addBtn.setVisibility(View.INVISIBLE);
            holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.VISIBLE);
        } else {
            // If the item is not in the order, show the addBtn and hide the itemBtnIncreaseQtyDecreaseQtyholder
            holder.addBtn.setVisibility(View.VISIBLE);
            holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.INVISIBLE);
        }

        holder.decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemQuantity = item.getItemQuantity();
                if (itemQuantity <= 1) {
                    holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.GONE);
                    holder.addBtn.setVisibility(View.VISIBLE);
                    order.calculateTotal();
                    order.removeItem(item);
                    updateViewCartVisibility();
                    notifyDataSetChanged(); // Notify adapter of dataset change
                } else {
                    item.decreaseQuantity();
                    holder.itemQuantity.setText(String.valueOf(item.getItemQuantity()));
                    updateViewCartVisibility();
                    notifyDataSetChanged(); // Notify adapter of dataset change
                }
                holder.decreaseQuantity.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.increaseQuantity.setEnabled(true);
                    }
                }, 1000); // Adjust the delay as needed
            }
        });
        holder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the button to prevent multiple clicks
                holder.increaseQuantity.setEnabled(false);

                int itemQuantity = item.getItemQuantity();
                if (itemQuantity >= 5) {
                    Toast.makeText(context, "Max 5 Qty Allowed", Toast.LENGTH_SHORT).show();
                } else {
                    item.increaseQuantity();
                    holder.itemQuantity.setText(String.valueOf(item.getItemQuantity()));
                    updateViewCartVisibility();
                    notifyDataSetChanged(); // Notify adapter of dataset change
                }

                // Re-enable the button after a delay or when the operation is completed
                holder.increaseQuantity.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.increaseQuantity.setEnabled(true);
                    }
                }, 1000); // Adjust the delay as needed
            }
        });

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isItemInOrder(item)) {
                    // Disable the button to prevent multiple clicks
                    holder.addBtn.setEnabled(false);
                    order.addItem(item);
                    holder.addBtn.setVisibility(View.GONE);
                    holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.VISIBLE);
                    item.increaseQuantity();
                    updateViewCartVisibility();
                    notifyDataSetChanged(); // Notify adapter of dataset change

                    // Re-enable the button after a delay or when the operation is completed
                    holder.addBtn.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.addBtn.setEnabled(true);
                        }
                    }, 1000); // Adjust the delay as needed
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTextView;
        public TextView foodDescTextView;
        public TextView priceTextView;
        public TextView itemQuantity;
        ImageView itemImageView;
        Button addBtn, increaseQuantity, decreaseQuantity;
        LinearLayout itemBtnIncreaseQtyDecreaseQtyholder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            foodNameTextView = itemView.findViewById(R.id.foodName);
            foodDescTextView = itemView.findViewById(R.id.foodDesc);
            priceTextView = itemView.findViewById(R.id.price);
            addBtn = itemView.findViewById(R.id.addBtn);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decraseQuantity);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemBtnIncreaseQtyDecreaseQtyholder = itemView.findViewById(R.id.itemBtnIncreaseQtyDecreaseQtyholder);
        }
    }

    private boolean isItemInOrder(Item item) {
        List<Item> orderItems = order.getItems();

        // Loop through each item in the orderItems list
        for (Item orderItem : orderItems) {
            // Check if the item_id of the current orderItem matches the item_id of the item we're looking for
            if (orderItem.getItemId().equals(item.getItemId())) {
                // Item with the same ID found in the order, return true
                return true;
            }
        }
        // Item with the same ID not found in the order, return false
        return false;
    }

    private void updateViewCartVisibility() {
        MainActivity mainActivity = (MainActivity) context;
        if (mainActivity != null) {
            FrameLayout viewCartFrameLayoutMenu = mainActivity.getViewCartFrameLayoutMenu();
            if (order.itemQuantity() > 0) {
                viewCartFrameLayoutMenu.setVisibility(View.VISIBLE);
                updateItemCount();
                updatePrice();
            } else {
                viewCartFrameLayoutMenu.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateItemCount() {
        MainActivity mainActivity = (MainActivity) context;
        if (mainActivity != null) {
            TextView viewCartTextView = mainActivity.getViewCartQuantityTextView();
            if (order.itemQuantity() > 0) {
                viewCartTextView.setText(String.valueOf(order.itemQuantity() + " Items   |"));
            } else {
                viewCartTextView.setText(String.valueOf(order.itemQuantity()));
            }
        }
    }

    private void updatePrice() {
        MainActivity mainActivity = (MainActivity) context;
        if (mainActivity != null) {
            TextView viewCartPriceTextView = mainActivity.getViewCartPriceTextView();
            viewCartPriceTextView.setText(String.valueOf("₹" + order.calculateTotal()));
        }
    }

    public void setData(List<Item> newData) {
        this.itemList = newData;
        notifyDataSetChanged(); // Notify adapter of dataset change
    }
}
