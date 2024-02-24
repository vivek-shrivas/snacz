package com.example.snacz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class SidebarAdapter extends RecyclerView.Adapter<SidebarAdapter.ViewHolder> {
    private List<Category> categoryList;
    private Context context;
    private OnCategorySelectedListener onCategorySelectedListener;

    public SidebarAdapter(Context context, List<Category> categoryList, OnCategorySelectedListener onCategorySelectedListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.onCategorySelectedListener = onCategorySelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_sidebar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sidebarImageView;
        TextView sidebarSubtextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sidebarImageView = itemView.findViewById(R.id.menu_sidebar_img);
            sidebarSubtextView = itemView.findViewById(R.id.menu_sidebar_subtext);
        }

        public void bind(Category category) {
            Glide.with(context)
                    .load(category.getCategoryImage())
                    .into(sidebarImageView);

            // Set the category name to the TextView
            sidebarSubtextView.setText(category.getCategoryName());

            // Set an onClickListener for the itemView
            itemView.setOnClickListener(v -> {
                // Call the onCategorySelected method of the listener
                if (onCategorySelectedListener != null) {
                    onCategorySelectedListener.onCategorySelected(category.getCategoryName());
                }
            });
        }
    }

    // Interface for category selection listener
    public interface OnCategorySelectedListener {
        void onCategorySelected(String category);
    }
}
