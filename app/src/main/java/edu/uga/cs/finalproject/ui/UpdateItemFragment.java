package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.finalproject.R;

public class UpdateItemFragment extends Fragment {

    private TextView theCategoryTextView;
    private EditText theNameEditText;
    private EditText theDescriptionEditText;
    private CheckBox theFreeCheckBox;
    private EditText thePriceEditText;
    private Button theUpdateButton;

    private DatabaseReference theDatabase;
    private String theItemId;
    private String theCategoryId;
    private String theCategoryName;
    private String theSellerId;

    private String theCurrentName;
    private String theCurrentDescription;
    private double theCurrentPrice;
    private boolean theCurrentIsFree;

    public UpdateItemFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater theInflater, @Nullable ViewGroup theContainer,
                             @Nullable Bundle theSavedInstanceState) {
        return theInflater.inflate(R.layout.fragment_update_item, theContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View theView, @Nullable Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);

        theDatabase = FirebaseDatabase.getInstance().getReference("items");

        theCategoryTextView = theView.findViewById(R.id.textViewCategory);
        theNameEditText = theView.findViewById(R.id.editTextItemName);
        theDescriptionEditText = theView.findViewById(R.id.editTextItemDescription);
        theFreeCheckBox = theView.findViewById(R.id.checkBoxFree);
        thePriceEditText = theView.findViewById(R.id.editTextPrice);
        theUpdateButton = theView.findViewById(R.id.buttonUpdateItem);

        if (getArguments() != null) {
            theItemId = getArguments().getString("itemId");
            theCategoryId = getArguments().getString("categoryId");
            theCategoryName = getArguments().getString("categoryName");
            theSellerId = getArguments().getString("sellerId");

            theCurrentName = getArguments().getString("name");
            theCurrentDescription = getArguments().getString("description");
            theCurrentPrice = getArguments().getDouble("price");
            theCurrentIsFree = getArguments().getBoolean("isFree");

            theCategoryTextView.setText("Category: " + theCategoryName);
            theNameEditText.setText(theCurrentName);
            theDescriptionEditText.setText(theCurrentDescription);
            theFreeCheckBox.setChecked(theCurrentIsFree);
            if (theCurrentIsFree) {
                thePriceEditText.setText("0.00");
                thePriceEditText.setEnabled(false);
            } else {
                thePriceEditText.setText(String.valueOf(theCurrentPrice));
            }
        }

        if (theSavedInstanceState != null) {
            theNameEditText.setText(theSavedInstanceState.getString("savedName", theNameEditText.getText().toString()));
            theDescriptionEditText.setText(theSavedInstanceState.getString("savedDescription", theDescriptionEditText.getText().toString()));
            boolean savedFree = theSavedInstanceState.getBoolean("savedIsFree", theFreeCheckBox.isChecked());
            theFreeCheckBox.setChecked(savedFree);
            thePriceEditText.setText(theSavedInstanceState.getString("savedPrice", thePriceEditText.getText().toString()));
            thePriceEditText.setEnabled(!savedFree);
        }

        theFreeCheckBox.setOnCheckedChangeListener((theButtonView, theIsChecked) -> {
            if (theIsChecked) {
                thePriceEditText.setText("0.00");
                thePriceEditText.setEnabled(false);
            } else {
                thePriceEditText.setEnabled(true);
            }
        });

        theUpdateButton.setOnClickListener(theV -> updateItem());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedName", theNameEditText.getText().toString());
        outState.putString("savedDescription", theDescriptionEditText.getText().toString());
        outState.putString("savedPrice", thePriceEditText.getText().toString());
        outState.putBoolean("savedIsFree", theFreeCheckBox.isChecked());
    }

    private void updateItem() {
        String theName = theNameEditText.getText().toString().trim();
        String theDescription = theDescriptionEditText.getText().toString().trim();
        String thePriceStr = thePriceEditText.getText().toString().trim();
        boolean theIsFree = theFreeCheckBox.isChecked();

        if (TextUtils.isEmpty(theName)) {
            theNameEditText.setError("The name is required");
            return;
        }

        if (!theIsFree && TextUtils.isEmpty(thePriceStr)) {
            thePriceEditText.setError("The price is required");
            return;
        }

        double thePrice = 0.0;
        if (!theIsFree) {
            try {
                thePrice = Double.parseDouble(thePriceStr);
            } catch (NumberFormatException theE) {
                thePriceEditText.setError("Sorry Invalid price");
                return;
            }
        }

        FirebaseUser theCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (theCurrentUser == null || !theCurrentUser.getUid().equals(theSellerId)) {
            Toast.makeText(getContext(), "You can only update your own items", Toast.LENGTH_SHORT).show();
            return;
        }

        theDatabase.child(theItemId).child("name").setValue(theName);
        theDatabase.child(theItemId).child("description").setValue(theDescription);
        theDatabase.child(theItemId).child("price").setValue(thePrice);
        theDatabase.child(theItemId).child("free").setValue(theIsFree);

        Toast.makeText(getContext(), "The item updated", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(requireView()).navigateUp();
    }
}

