package edu.uga.cs.finalproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    public interface OnCategoryAction {
        void onClick(Category c);

        void onEdit(Category c);

        void onDelete(Category c);
    }

    private List<Category> items;
    private OnCategoryAction actionListener;
    private String currentUserId;

    public CategoryAdapter(List<Category> items, String currentUserId, OnCategoryAction actionListener) {
        this.items = items;
        this.currentUserId = currentUserId;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Category c = items.get(position);
        holder.name.setText(c.getName());

        holder.itemView.setOnClickListener(v -> actionListener.onClick(c));

        if (currentUserId != null && currentUserId.equals(c.getCreatedBy())) {
            holder.editBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.VISIBLE);

            holder.editBtn.setOnClickListener(v -> actionListener.onEdit(c));
            holder.deleteBtn.setOnClickListener(v -> actionListener.onDelete(c));
        } else {
            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton editBtn, deleteBtn;

        VH(View v) {
            super(v);
            name = v.findViewById(R.id.categoryName);
            editBtn = v.findViewById(R.id.buttonEdit);
            deleteBtn = v.findViewById(R.id.buttonDelete);
        }
    }
}
