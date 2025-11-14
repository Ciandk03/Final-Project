package edu.uga.cs.finalproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uga.cs.finalproject.models.Category;
import edu.uga.cs.finalproject.R;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    public interface OnClick { void onClick(Category c); }
    private List<Category> items;
    private OnClick onClick;

    public CategoryAdapter(List<Category> items, OnClick onClick){
        this.items = items; this.onClick = onClick;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int position){
        Category c = items.get(position);
        holder.name.setText(c.name);
        holder.itemView.setOnClickListener(v -> onClick.onClick(c));
    }

    @Override public int getItemCount(){ return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name;
        VH(View v){ super(v); name = v.findViewById(R.id.categoryName); }
    }
}
