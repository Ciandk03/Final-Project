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

public class UpdateCategoryFragment extends Fragment {

    private EditText theCategoryNameEditText;
    private Button theUpdateCategoryButton;
    private DatabaseReference theMDatabase;
    private String theCategoryId;
    private String theCurrentName;

    public UpdateCategoryFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        theMDatabase = FirebaseDatabase.getInstance().getReference("categories");

        theCategoryNameEditText = view.findViewById(R.id.editTextCategoryName);
        theUpdateCategoryButton = view.findViewById(R.id.buttonUpdateCategory);

        if (getArguments() != null) {
            theCategoryId = getArguments().getString("categoryId");
            theCurrentName = getArguments().getString("categoryName");
            theCategoryNameEditText.setText(theCurrentName);
        }

        theUpdateCategoryButton.setOnClickListener(v -> updateCategory());
    }

    private void updateCategory() {
        String newName = theCategoryNameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(newName)) {
            theCategoryNameEditText.setError("The category name is required");
            return;
        }

        theMDatabase.child(theCategoryId).child("Name").setValue(newName)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "The category updated", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigateUp();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
