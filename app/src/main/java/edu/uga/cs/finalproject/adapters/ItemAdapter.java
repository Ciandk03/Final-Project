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

        theHolder.itemView.setOnClickListener(theV -> theActionListener.onClick(theItem));

        if (theCurrentUserId != null && theCurrentUserId.equals(theItem.getSellerId())) {
            theHolder.theDeleteBtn.setVisibility(View.VISIBLE);
            theHolder.theDeleteBtn.setOnClickListener(theV -> theActionListener.onDelete(theItem));
        } else {
            theHolder.theDeleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return theItems.size();
    }

    static class theVH extends RecyclerView.ViewHolder {
        TextView theTitle, thePrice, theSeller, theDate;
        ImageButton theDeleteBtn;

        theVH(View theView) {
            super(theView);
            theTitle = theView.findViewById(R.id.cardTitle);
            thePrice = theView.findViewById(R.id.cardPrice);
            theSeller = theView.findViewById(R.id.cardSeller);
            theDate = theView.findViewById(R.id.cardDate);
            theDeleteBtn = theView.findViewById(R.id.buttonDeleteItem);
        }
    }
}

