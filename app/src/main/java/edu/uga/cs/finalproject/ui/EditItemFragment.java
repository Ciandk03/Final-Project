package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.finalproject.R;

public class EditItemFragment extends Fragment {

    private EditText nameEditText, priceEditText;
    private Button saveButton;
    private DatabaseReference mDatabase;
    private String itemId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference("items");
        nameEditText = view.findViewById(R.id.editTextItemName);
        priceEditText = view.findViewById(R.id.editTextItemPrice);
        saveButton = view.findViewById(R.id.buttonSaveItem);

        if (getArguments() != null) {
            itemId = getArguments().getString("itemId");
            nameEditText.setText(getArguments().getString("itemName"));
            double price = getArguments().getDouble("itemPrice", 0.0);
            if (price > 0) {
                priceEditText.setText(String.valueOf(price));
            }
        }

        if (savedInstanceState != null) {
            nameEditText.setText(savedInstanceState.getString("savedItemName", ""));
            priceEditText.setText(savedInstanceState.getString("savedItemPrice", ""));
        }

        saveButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            if (TextUtils.isEmpty(newName)) {
                nameEditText.setError("Name required");
                return;
            }

            double newPrice = TextUtils.isEmpty(priceStr) ? 0.0 : Double.parseDouble(priceStr);
            boolean isFree = TextUtils.isEmpty(priceStr);

            mDatabase.child(itemId).child("name").setValue(newName);
            mDatabase.child(itemId).child("price").setValue(newPrice);
            mDatabase.child(itemId).child("free").setValue(isFree)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Item updated", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).navigateUp();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getActivity(), "Update failed", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedItemName", nameEditText.getText().toString());
        outState.putString("savedItemPrice", priceEditText.getText().toString());
    }
}

