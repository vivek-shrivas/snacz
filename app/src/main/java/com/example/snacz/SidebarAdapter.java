package com.example.snacz;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SidebarAdapter extends RecyclerView.Adapter<SidebarAdapter.ViewHolder> {
    private List<Category> categoryList;
    private Context context;

    public SidebarAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
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
            category.setCategoryImage(category.getCategoryImage());
            category.setCategoryName(category.getCategoryName());
        }
    }
}
