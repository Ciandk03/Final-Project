package edu.uga.cs.finalproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.VH> {

    public interface OnConfirmListener {
        void onConfirm(Transaction t);
    }

    private List<Transaction> items;
    private String currentUserId;
    private OnConfirmListener confirmListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public TransactionAdapter(List<Transaction> items, String currentUserId, OnConfirmListener confirmListener) {
        this.items = items;
        this.currentUserId = currentUserId;
        this.confirmListener = confirmListener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Transaction t = items.get(position);

        holder.title.setText(t.getItemTitle());
        holder.price.setText(String.format("$%.2f", t.getItemPrice()));
        holder.date.setText(sdf.format(new Date(t.getInitiatedAt())));

        holder.category.setText("Category: " + (t.getCategoryName() != null ? t.getCategoryName() : "Unknown"));

        boolean isSeller = currentUserId.equals(t.getSellerId());
        if (isSeller) {
            holder.otherParty.setText("Buyer: " + (t.getBuyerName() != null ? t.getBuyerName() : "Unknown"));
        } else {
            holder.otherParty.setText("Seller: " + (t.getSellerName() != null ? t.getSellerName() : "Unknown"));
        }

        if ("PENDING".equals(t.getStatus())) {
            boolean myConfirmed = isSeller ? t.isSellerConfirmed() : t.isBuyerConfirmed();
            if (!myConfirmed) {
                holder.confirmBtn.setVisibility(View.VISIBLE);
                holder.statusTv.setVisibility(View.GONE);
                holder.confirmBtn.setOnClickListener(v -> confirmListener.onConfirm(t));
            } else {
                holder.confirmBtn.setVisibility(View.GONE);
                holder.statusTv.setVisibility(View.VISIBLE);
                holder.statusTv.setText("Waiting for other party...");
            }
        } else {
            // Completed
            holder.confirmBtn.setVisibility(View.GONE);
            holder.statusTv.setVisibility(View.VISIBLE);
            holder.statusTv.setText("Completed on " + sdf.format(new Date(t.getCompletedAt())));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, price, category, otherParty, date, statusTv;
        Button confirmBtn;

        VH(View v) {
            super(v);
            title = v.findViewById(R.id.transItemTitle);
            price = v.findViewById(R.id.transPrice);
            category = v.findViewById(R.id.transCategory);
            otherParty = v.findViewById(R.id.transOtherParty);
            date = v.findViewById(R.id.transDate);
            statusTv = v.findViewById(R.id.tvStatus);
            confirmBtn = v.findViewById(R.id.btnConfirm);
        }
    }
}
