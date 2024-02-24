package com.example.snacz;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.snacz.Item;
import com.example.snacz.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Item> itemList;

    public CartAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the item from the itemList at the specified position
        Item item = itemList.get(position);
        holder.foodNameTextView.setText(item.getItemName());
        holder.foodDescTextView.setText(item.getItemDesc());
        Glide.with(holder.itemView.getContext())
                .load(item.getItemImage())
                .into(holder.itemImageView);
        holder.priceTextView.setText(String.valueOf("₹"+item.getPrice()));

        // You can bind other views as needed
    }


    public void setData(List<Item> newData) {
        this.itemList = newData;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView foodDescTextView;
        TextView priceTextView;
        TextView itemQuantity;
        ImageView itemImageView;
        Button addBtn,increaseQuantity,decreaseQuantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodName);
            foodDescTextView = itemView.findViewById(R.id.foodDesc);
            priceTextView = itemView.findViewById(R.id.price);
            priceTextView = itemView.findViewById(R.id.price);
            addBtn = itemView.findViewById(R.id.addBtn);
            increaseQuantity=itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity=itemView.findViewById(R.id.decraseQuantity);
            itemQuantity=itemView.findViewById(R.id.itemQuantity);
            itemImageView=itemView.findViewById(R.id.itemImageView);
        }
    }
}
