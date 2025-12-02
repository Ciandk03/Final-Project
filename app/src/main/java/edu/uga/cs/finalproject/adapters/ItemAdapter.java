package edu.uga.cs.finalproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.theVH> {

    public interface theOnItemAction {
        void onClick(Item theItem);
        void onDelete(Item theItem);
        void onEdit(Item theItem); // existing
        void onBuy(Item theItem);  // NEW
    }

    private List<Item> theItems;
    private theOnItemAction theActionListener;
    private String theCurrentUserId;
    private SimpleDateFormat theSdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());

    public ItemAdapter(List<Item> theItems, String theCurrentUserId, theOnItemAction theActionListener) {
        this.theItems = theItems;
        this.theCurrentUserId = theCurrentUserId;
        this.theActionListener = theActionListener;
    }

    @NonNull
    @Override
    public theVH onCreateViewHolder(@NonNull ViewGroup theParent, int theViewType) {
        View theView = LayoutInflater.from(theParent.getContext()).inflate(R.layout.item_card, theParent, false);
        return new theVH(theView);
    }

    @Override
    public void onBindViewHolder(@NonNull theVH theHolder, int thePosition) {
        Item theItem = theItems.get(thePosition);
        theHolder.theTitle.setText(theItem.getName());
        theHolder.thePrice.setText(theItem.isFree() ? "Free" : String.format("$%.2f", theItem.getPrice()));
        theHolder.theSeller.setText("Posted by: " + (theItem.getSellerName() != null ? theItem.getSellerName() : "Unknown"));
        theHolder.theDate.setText(theSdf.format(new Date(theItem.getCreatedAt())));

        // Handle click differently for buyer vs seller
        theHolder.itemView.setOnClickListener(theV -> {
            if (theCurrentUserId != null && !theCurrentUserId.equals(theItem.getSellerId())) {
                // Buyer clicked → trigger buy/accept
                theActionListener.onBuy(theItem);
            } else {
                // Seller clicked → normal detail view
                theActionListener.onClick(theItem);
            }
        });

        if (theCurrentUserId != null && theCurrentUserId.equals(theItem.getSellerId())) {
            theHolder.theDeleteBtn.setVisibility(View.VISIBLE);
            theHolder.theDeleteBtn.setOnClickListener(theV -> theActionListener.onDelete(theItem));

            theHolder.theEditBtn.setVisibility(View.VISIBLE);
            theHolder.theEditBtn.setOnClickListener(theV -> theActionListener.onEdit(theItem));
        } else {
            theHolder.theDeleteBtn.setVisibility(View.GONE);
            theHolder.theEditBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return theItems.size();
    }

    static class theVH extends RecyclerView.ViewHolder {
        TextView theTitle, thePrice, theSeller, theDate;
        ImageButton theDeleteBtn, theEditBtn;

        theVH(View theView) {
            super(theView);
            theTitle = theView.findViewById(R.id.cardTitle);
            thePrice = theView.findViewById(R.id.cardPrice);
            theSeller = theView.findViewById(R.id.cardSeller);
            theDate = theView.findViewById(R.id.cardDate);
            theDeleteBtn = theView.findViewById(R.id.buttonDeleteItem);
            theEditBtn = theView.findViewById(R.id.buttonEditItem);
        }
    }
}
