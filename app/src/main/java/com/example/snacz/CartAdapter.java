package com.example.snacz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Item> itemList;

    private Order order;
    private Context context;

    public CartAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        this.order = Order.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.foodNameTextView.setText(item.getItemName());
        holder.foodDescTextView.setText(item.getItemDesc());
        holder.priceTextView.setText(String.valueOf(item.getPrice()));

        if (holder.itemView.getContext() != null && item.getItemImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getItemImage())
                    .into(holder.itemImageView);
        }
    }

    public void setData(List<Item> newData) {
        this.itemList = newData;
        notifyDataSetChanged(); // Notify adapter of dataset change
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        ImageView itemImageView;
        TextView foodDescTextView;
        TextView priceTextView;
        TextView itemQuantity;
        Button increaseQuantity, decreaseQuantity;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodName);
            foodDescTextView = itemView.findViewById(R.id.foodDesc);
            priceTextView = itemView.findViewById(R.id.price);
            increaseQuantity = itemView.findViewById(R.id.increaseQuantity);
            decreaseQuantity = itemView.findViewById(R.id.decraseQuantity);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemImageView=itemView.findViewById(R.id.itemImageView);

            increaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Item item = itemList.get(position); // Access itemList from the instance
                        item.increaseQuantity();
                        itemQuantity.setText(String.valueOf(item.getItemQuantity()));
                        ((Cart)context).updateTotalPayable(); // Update total payable in Cart activity
                    }
                }
            });

            decreaseQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Item item = itemList.get(position); // Access itemList from the instance
                        if (item.getItemQuantity() > 0) {
                            item.decreaseQuantity();
                            itemQuantity.setText(String.valueOf(item.getItemQuantity()));
                            if (item.getItemQuantity() < 1) {
                                itemList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, itemList.size());
                            }
                            ((Cart)context).updateTotalPayable(); // Update total payable in Cart activity
                        }
                    }
                }
            });
        }
    }
}
