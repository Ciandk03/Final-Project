package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.Item;

public class AddItemFragment extends Fragment {

    private TextView categoryTextView;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private CheckBox freeCheckBox;
    private EditText priceEditText;
    private Button postButton;

    private String categoryId;
    private String categoryName;
    private DatabaseReference mDatabase;

    public AddItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            categoryName = getArguments().getString("categoryName");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("items");

        categoryTextView = view.findViewById(R.id.textViewCategory);
        nameEditText = view.findViewById(R.id.editTextItemName);
        descriptionEditText = view.findViewById(R.id.editTextItemDescription);
        freeCheckBox = view.findViewById(R.id.checkBoxFree);
        priceEditText = view.findViewById(R.id.editTextPrice);
        postButton = view.findViewById(R.id.buttonPostItem);

        categoryTextView.setText("Category: " + categoryName);

        freeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    priceEditText.setText("0.00");
                    priceEditText.setEnabled(false);
                } else {
                    priceEditText.setEnabled(true);
                    priceEditText.setText("");
                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postItem();
            }
        });
    }

    private void postItem() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        boolean isFree = freeCheckBox.isChecked();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }

        if (!isFree && TextUtils.isEmpty(priceStr)) {
            priceEditText.setError("Price is required");
            return;
        }

        double price = 0.0;
        if (!isFree) {
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                priceEditText.setError("Invalid price");
                return;
            }
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "You must be logged in to post an item", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = mDatabase.push().getKey();
        Item item = new Item(key, name, description, price, isFree, categoryId, categoryName, currentUser.getUid(),
                currentUser.getEmail());

        mDatabase.child(key).setValue(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Item posted", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigateUp();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to post item: " + e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
