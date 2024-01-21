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
    private List<SidebarItem> sidebarItems;
    private Context context;

    public SidebarAdapter(Context context, List<SidebarItem> sidebarItems) {
        this.context = context;
        this.sidebarItems = sidebarItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_sidebar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SidebarItem sidebarItem = sidebarItems.get(position);
        holder.bind(sidebarItem);
    }

    @Override
    public int getItemCount() {
        return sidebarItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sidebarImageView;
        TextView sidebarSubtextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sidebarImageView = itemView.findViewById(R.id.menu_sidebar_img);
            sidebarSubtextView = itemView.findViewById(R.id.menu_sidebar_subtext);
        }

        public void bind(SidebarItem sidebarItem) {
            sidebarImageView.setImageResource(sidebarItem.getImageResId());
            sidebarSubtextView.setText(sidebarItem.getSubtext());
        }
    }
}
