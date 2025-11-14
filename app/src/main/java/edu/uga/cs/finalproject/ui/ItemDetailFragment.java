package edu.uga.cs.finalproject.ui;

/*

 - Shows item details and "Agree to buy" button
 - argument: itemId
*/

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.Item;

public class ItemDetailFragment extends Fragment {

    private String itemId;
    private Item item; // load from DB in TODO
    private TextView titleTv, descTv, priceTv, postedByTv;
    private Button agreeBtn;

    public ItemDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        if(getArguments()!=null) itemId = getArguments().getString("itemId");

        titleTv = view.findViewById(R.id.itemTitle);
        descTv = view.findViewById(R.id.itemDescription);
        priceTv = view.findViewById(R.id.itemPrice);
        postedByTv = view.findViewById(R.id.itemPostedBy);
        agreeBtn = view.findViewById(R.id.agreeButton);

        loadItem();

        agreeBtn.setOnClickListener(v -> {
            // TODO: Create a transaction document, set item.status = "pending", notify seller
            // For now show a placeholder navigation back
            Navigation.findNavController(v).popBackStack();
        });
    }

    private void loadItem(){
        // TODO: Load item from Firestore by itemId
        // placeholder:
        item = new Item();
        item.title = "Sample Couch";
        item.description = "Large couch - pick up only";
        item.isFree = false;
        item.priceCents = 15000;
        item.postedByName = "Alice";

        titleTv.setText(item.title);
        descTv.setText(item.description);
        priceTv.setText(item.isFree ? "Free" : String.format("$%.2f", item.priceCents / 100.0));
        postedByTv.setText("Posted by: " + item.postedByName);
    }
}
