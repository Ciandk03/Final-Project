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

public class EditCategoryFragment extends Fragment {

    private EditText categoryNameEditText;
    private Button saveButton;
    private DatabaseReference mDatabase;
    private String categoryId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference("categories");
        categoryNameEditText = view.findViewById(R.id.editTextCategoryName);
        saveButton = view.findViewById(R.id.buttonSaveCategory);

        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            categoryNameEditText.setText(getArguments().getString("categoryName"));
        }

        if (savedInstanceState != null) {
            categoryNameEditText.setText(savedInstanceState.getString("savedCategoryName", ""));
        }

        saveButton.setOnClickListener(v -> {
            String newName = categoryNameEditText.getText().toString().trim();
            if (TextUtils.isEmpty(newName)) {
                categoryNameEditText.setError("Name required");
                return;
            }
            mDatabase.child(categoryId).child("name").setValue(newName)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Category updated", Toast.LENGTH_SHORT).show();
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
        outState.putString("savedCategoryName", categoryNameEditText.getText().toString());
    }
}
