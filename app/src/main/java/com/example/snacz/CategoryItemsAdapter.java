package com.example.snacz;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

public class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.ViewHolder> {
    private List<Item> itemList;
    public TextView viewCartItemQuantity;

    public CategoryItemsAdapter(List<Item> itemList) {
        this.itemList = itemList;
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
        holder.priceTextView.setText(String.valueOf("₹"+item.getPrice()));
        if (isItemInOrder(item)) {
            holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.VISIBLE);// Disable button if item is already in cart
            holder.addBtn.setVisibility(View.GONE);
        } else {
            holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.GONE);
            holder.addBtn.setVisibility(View.VISIBLE); // Enable button if item is not in cart
        }

        holder.decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemQuantity=item.getItemQuantity();
                if(itemQuantity<=1){
                    holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.GONE);
                    holder.addBtn.setVisibility(View.VISIBLE);
                    Order order=Order.getInstance();
                    order.calculateTotal();
                    order.removeItem(item);
                    Toast removeToast =Toast.makeText(v.getContext(), "Item removed from cart",Toast.LENGTH_SHORT);
                    removeToast.show();
                }
                else{
                    item.decreaseQuantity();
                    holder.itemQuantity.setText(String.valueOf(item.getItemQuantity()));
                    Toast decreaseToast =Toast.makeText(v.getContext(), "Quantity Updated",Toast.LENGTH_SHORT);
                    decreaseToast.show();
                }

            }
        });


        holder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemQuantity=item.getItemQuantity();
                if(itemQuantity>=5){
                    Toast toast = Toast.makeText(v.getContext(),"Max 5 Qty Allowed",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    item.increaseQuantity();
                    holder.itemQuantity.setText(String.valueOf(item.getItemQuantity()));
                    Toast increaseToast =Toast.makeText(v.getContext(), "Quantity Updated in cart",Toast.LENGTH_SHORT);
                    increaseToast.show();
                }

            }
        });
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isItemInOrder(item)) {
                    Order order = Order.getInstance();
                    order.addItem(item);
                    holder.addBtn.setVisibility(View.GONE);
                    holder.itemBtnIncreaseQtyDecreaseQtyholder.setVisibility(View.VISIBLE);
                    String name=item.getItemName();
                    item.increaseQuantity();
                    Toast.makeText(v.getContext(), "Added "+name+" to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Item is already in the cart", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        Log.d("ItemCountDebug", "Item count: " + itemList.size());
        return itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTextView;
        public TextView foodDescTextView;
        public TextView priceTextView;
        public TextView itemQuantity;
        ImageView itemImageView;
        Button addBtn ,increaseQuantity ,decreaseQuantity;
        LinearLayout itemBtnIncreaseQtyDecreaseQtyholder;
        FrameLayout viewCartFrameLayoutMenu;

        TextView viewCartItemQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView=itemView.findViewById(R.id.itemImageView);
            foodNameTextView = itemView.findViewById(R.id.foodName);
            foodDescTextView = itemView.findViewById(R.id.foodDesc);
            priceTextView = itemView.findViewById(R.id.price);
            addBtn = itemView.findViewById(R.id.addBtn);
            increaseQuantity=itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity=itemView.findViewById(R.id.decraseQuantity);
            itemQuantity=itemView.findViewById(R.id.itemQuantity);
            itemBtnIncreaseQtyDecreaseQtyholder=itemView.findViewById(R.id.itemBtnIncreaseQtyDecreaseQtyholder);
            viewCartFrameLayoutMenu=itemView.findViewById(R.id.viewCartFrameLayoutMenu);
        }

    }
    private boolean isItemInOrder(Item item) {
        Order order = Order.getInstance();
        List<Item> orderItems = order.getItems();

        // Loop through each item in the orderItems list
        for (Item orderItem : orderItems) {
            // Check if the item_id of the current orderItem matches the item_id of the item we're looking for
            if (Objects.equals(orderItem.getItemId(), item.getItemId())) {
                // Item with the same ID found in the order, return true
                return true;
            }
        }
        // Item with the same ID not found in the order, return false
        return false;
    }
}
