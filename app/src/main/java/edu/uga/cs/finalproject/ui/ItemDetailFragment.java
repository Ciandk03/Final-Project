package edu.uga.cs.finalproject.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.Item;
import edu.uga.cs.finalproject.models.Transaction;

public class ItemDetailFragment extends Fragment {

    private String itemId;
    private Item item;
    private TextView titleTv, descTv, priceTv, postedByTv;
    private Button agreeBtn, editBtn, deleteBtn;
    private LinearLayout sellerActionsLayout;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    public ItemDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            itemId = getArguments().getString("itemId");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        titleTv = view.findViewById(R.id.itemTitle);
        descTv = view.findViewById(R.id.itemDescription);
        priceTv = view.findViewById(R.id.itemPrice);
        postedByTv = view.findViewById(R.id.itemPostedBy);
        agreeBtn = view.findViewById(R.id.agreeButton);
        editBtn = view.findViewById(R.id.editButton);
        deleteBtn = view.findViewById(R.id.deleteButton);
        sellerActionsLayout = view.findViewById(R.id.sellerActionsLayout);

        if (savedInstanceState != null) {
            itemId = savedInstanceState.getString("savedItemId", itemId);
            String name = savedInstanceState.getString("savedItemName");
            String desc = savedInstanceState.getString("savedItemDesc");
            double price = savedInstanceState.getDouble("savedItemPrice", 0.0);
            boolean isFree = savedInstanceState.getBoolean("savedItemFree", false);
            String sellerName = savedInstanceState.getString("savedItemSellerName");
            String sellerId = savedInstanceState.getString("savedItemSellerId");
            String status = savedInstanceState.getString("savedItemStatus");

            if (name != null) {
                item = new Item();
                item.setId(itemId);
                item.setName(name);
                item.setDescription(desc);
                item.setPrice(price);
                item.setFree(isFree);
                item.setSellerName(sellerName);
                item.setSellerId(sellerId);
                item.setStatus(status);
                updateUI();
            } else {
                loadItem();
            }
        } else {
            loadItem();
        }

        agreeBtn.setOnClickListener(v -> initiateTransaction());
        editBtn.setOnClickListener(v -> showEditDialog());
        deleteBtn.setOnClickListener(v -> deleteItem());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedItemId", itemId);
        if (item != null) {
            outState.putString("savedItemName", item.getName());
            outState.putString("savedItemDesc", item.getDescription());
            outState.putDouble("savedItemPrice", item.getPrice());
            outState.putBoolean("savedItemFree", item.isFree());
            outState.putString("savedItemSellerName", item.getSellerName());
            outState.putString("savedItemSellerId", item.getSellerId());
            outState.putString("savedItemStatus", item.getStatus());
        }
    }

    private void loadItem() {
        mDatabase.child("items").child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                item = snapshot.getValue(Item.class);
                if (item != null) {
                    updateUI();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        titleTv.setText(item.getName());
        descTv.setText(item.getDescription());
        priceTv.setText(item.isFree() ? "Free" : String.format("$%.2f", item.getPrice()));
        postedByTv.setText("Posted by: " + (item.getSellerName() != null ? item.getSellerName() : "Unknown"));

        if (currentUser != null) {
            if (currentUser.getUid().equals(item.getSellerId())) {
                agreeBtn.setVisibility(View.GONE);
                sellerActionsLayout.setVisibility(View.VISIBLE);
            } else {
                sellerActionsLayout.setVisibility(View.GONE);
                if ("AVAILABLE".equals(item.getStatus())) {
                    agreeBtn.setVisibility(View.VISIBLE);
                } else {
                    agreeBtn.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initiateTransaction() {
        if (currentUser == null) return;

        String key = mDatabase.child("transactions").push().getKey();
        Transaction transaction = new Transaction(
                key,
                item.getId(),
                item.getName(),
                item.getCategoryName(),
                item.getPrice(),
                item.getSellerId(),
                item.getSellerName(),
                currentUser.getUid(),
                currentUser.getEmail()
        );

        item.setStatus("PENDING");
        mDatabase.child("items").child(item.getId()).setValue(item);

        mDatabase.child("transactions").child(key).setValue(transaction)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Transaction initiated", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).popBackStack();
                });
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Item");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);

        final EditText nameInput = new EditText(getContext());
        nameInput.setHint("Name");
        nameInput.setText(item.getName());
        layout.addView(nameInput);

        final EditText priceInput = new EditText(getContext());
        priceInput.setHint("Price");
        priceInput.setText(String.valueOf(item.getPrice()));
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (item.isFree()) {
            priceInput.setEnabled(false);
            priceInput.setText("0.0");
        }
        layout.addView(priceInput);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = nameInput.getText().toString().trim();
            String newPriceStr = priceInput.getText().toString().trim();

            if (!TextUtils.isEmpty(newName)) {
                item.setName(newName);
            }
            if (!item.isFree() && !TextUtils.isEmpty(newPriceStr)) {
                try {
                    item.setPrice(Double.parseDouble(newPriceStr));
                } catch (NumberFormatException e) {
                    // ignore invalid input
                }
            }

            mDatabase.child("items").child(item.getId()).setValue(item)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(getContext(), "Item updated", Toast.LENGTH_SHORT).show());
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteItem() {
        if (!"AVAILABLE".equals(item.getStatus())) {
            Toast.makeText(getContext(), "Cannot delete item in pending transaction", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Item")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    mDatabase.child("items").child(item.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getView()).popBackStack();
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

