package edu.uga.cs.finalproject.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.models.User;

public class RegisterFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        nameEditText = view.findViewById(R.id.editTextName);
        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        registerButton = view.findViewById(R.id.buttonRegister);
        loginTextView = view.findViewById(R.id.textViewLogin);

        if (savedInstanceState != null) {
            nameEditText.setText(savedInstanceState.getString("savedName", ""));
            emailEditText.setText(savedInstanceState.getString("savedEmail", ""));
            passwordEditText.setText(savedInstanceState.getString("savedPassword", ""));
        }

        registerButton.setOnClickListener(v -> registerUser());

        loginTextView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigateUp(); // Go back to login
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedName", nameEditText.getText().toString());
        outState.putString("savedEmail", emailEditText.getText().toString());
        outState.putString("savedPassword", passwordEditText.getText().toString());
    }

    private void registerUser() {
        final String name = nameEditText.getText().toString();
        final String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            writeNewUser(firebaseUser.getUid(), name, email);
                            Toast.makeText(getActivity(), "Registration successful", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView())
                                    .navigate(R.id.action_registerFragment_to_categoryListFragment);
                        } else {
                            Toast.makeText(getActivity(),
                                    "Registration failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User();
        user.uid = userId;
        user.displayName = name;
        user.email = email;
        // user.phone = ""; // Optional
        // user.createdAt = new Date(); // Optional

        mDatabase.child("users").child(userId).setValue(user);
    }
}
