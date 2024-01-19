package com.example.snacz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class menu_item_adapter extends RecyclerView.Adapter<menu_item_viewholder> {
    private List<menu_item_model> menu_item_modelList;
    public menu_item_adapter(List<menu_item_model> menu_item_modelList){
        this.menu_item_modelList=menu_item_modelList;
    }

    @NonNull
    @Override
    public menu_item_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_card,parent,false);
        return new menu_item_viewholder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull menu_item_viewholder holder, int position) {
        menu_item_model menuItemModel=menu_item_modelList.get(position);
        holder.bind(menuItemModel);

    }


    @Override
    public int getItemCount() {
        return menu_item_modelList.size();
    }

}
