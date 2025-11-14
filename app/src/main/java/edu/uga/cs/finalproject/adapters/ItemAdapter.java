package edu.uga.cs.finalproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uga.cs.finalproject.models.Item;
import edu.uga.cs.finalproject.R;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.VH> {

    public interface OnClick { void onClick(Item i); }
    private List<Item> items;
    private OnClick onClick;

    public ItemAdapter(List<Item> items, OnClick onClick){
        this.items = items; this.onClick = onClick;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH holder, int position){
        Item it = items.get(position);
        holder.title.setText(it.title);
        holder.price.setText(it.isFree ? "Free" : String.format("$%.2f", it.priceCents/100.0));
        holder.itemView.setOnClickListener(v -> onClick.onClick(it));
    }

    @Override public int getItemCount(){ return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, price;
        VH(View v){ super(v); title = v.findViewById(R.id.cardTitle); price = v.findViewById(R.id.cardPrice); }
    }
}
