package com.example.snacz;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class menu_item_viewholder extends RecyclerView.ViewHolder {

    private TextView foodName;
    private TextView foodDesc;
    private TextView price;

    public menu_item_viewholder(@NonNull View itemView) {
        super(itemView);
        foodName = itemView.findViewById(R.id.foodName);
        foodDesc=itemView.findViewById(R.id.foodDesc);
        price=itemView.findViewById(R.id.price);
    }
    public void bind(Item item){
        foodName.setText(item.getItemName());
        foodDesc.setText(item.getItemDesc());
        price.setText(item.getPrice());
    }


}
