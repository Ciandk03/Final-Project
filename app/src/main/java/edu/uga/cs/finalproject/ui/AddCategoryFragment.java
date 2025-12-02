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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.Category;

public class AddCategoryFragment extends Fragment {

    private EditText categoryNameEditText;
    private Button addCategoryButton;
    private DatabaseReference mDatabase;

    public AddCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference("categories");
        categoryNameEditText = view.findViewById(R.id.editTextCategoryName);
        addCategoryButton = view.findViewById(R.id.buttonAddCategory);

        if (savedInstanceState != null) {
            categoryNameEditText.setText(savedInstanceState.getString("savedCategoryName", ""));
        }

        addCategoryButton.setOnClickListener(v -> addCategory());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedCategoryName", categoryNameEditText.getText().toString());
    }

    private void addCategory() {
        String name = categoryNameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            categoryNameEditText.setError("Category name is required");
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "You must be logged in to add a category", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = mDatabase.push().getKey();
        Category category = new Category(key, name, currentUser.getUid());

        mDatabase.child(key).setValue(category)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Category added", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigateUp();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to add category: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}

