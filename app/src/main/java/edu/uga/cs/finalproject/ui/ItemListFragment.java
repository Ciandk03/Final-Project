package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.adapters.ItemAdapter;
import edu.uga.cs.finalproject.models.Item;

public class ItemListFragment extends Fragment {

    private RecyclerView theRecycler;
    private ItemAdapter theAdapter;
    private List<Item> theItems = new ArrayList<>();
    private String theCategoryId, theCategoryName;
    private DatabaseReference theDatabase;

    public ItemListFragment() {}

    @Override
    public View onCreateView(LayoutInflater theInflater, ViewGroup theContainer,
                             Bundle theSavedInstanceState) {
        return theInflater.inflate(R.layout.fragment_item_list, theContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        if (getArguments() != null) {
            theCategoryId = getArguments().getString("categoryId");
            theCategoryName = getArguments().getString("categoryName");
        }

        theDatabase = FirebaseDatabase.getInstance().getReference("items");

        theRecycler = theView.findViewById(R.id.itemRecycler);
        theRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        String theCurrentUserId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        theAdapter = new ItemAdapter(theItems, theCurrentUserId, new ItemAdapter.theOnItemAction() {
            @Override
            public void onClick(Item theItem) {
                Bundle theBundle = new Bundle();
                theBundle.putString("itemId", theItem.getId());
                Navigation.findNavController(theView).navigate(R.id.action_itemList_to_itemDetail, theBundle);
            }

            @Override
            public void onDelete(Item theItem) {
                theDatabase.child(theItem.getId()).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                            theItems.remove(theItem);
                            theAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        theRecycler.setAdapter(theAdapter);

        FloatingActionButton theFab = theView.findViewById(R.id.addItemFab);
        theFab.setOnClickListener(theV -> {
            Bundle theBundle = new Bundle();
            theBundle.putString("categoryId", theCategoryId);
            theBundle.putString("categoryName", theCategoryName);
            Navigation.findNavController(theV).navigate(R.id.action_itemList_to_addItem, theBundle);
        });

        loadItems();
    }

    private void loadItems() {
        Query theQuery = theDatabase.orderByChild("categoryId").equalTo(theCategoryId);
        theQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot theSnapshot) {
                theItems.clear();
                for (DataSnapshot theChild : theSnapshot.getChildren()) {
                    Item theItem = theChild.getValue(Item.class);
                    if (theItem != null && "AVAILABLE".equals(theItem.getStatus())) {
                        theItems.add(theItem);
                    }
                }
                Collections.sort(theItems, (theO1, theO2) -> Long.compare(theO2.getCreatedAt(), theO1.getCreatedAt()));
                theAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError theError) {
                Toast.makeText(getContext(), "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

