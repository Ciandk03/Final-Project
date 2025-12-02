package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uga.cs.finalproject.R;

public class BrowseFragment extends Fragment {

    // Example state field you might want to preserve (e.g., search query/filter)
    private String savedQuery = "";

    public BrowseFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            savedQuery = savedInstanceState.getString("savedQuery", "");
            // If you later add a search box or filter UI, set its text/value here
            // e.g., searchEditText.setText(savedQuery);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedQuery", savedQuery);
        // If you later add a search box, you can grab its current text here
        // e.g., outState.putString("savedQuery", searchEditText.getText().toString());
    }
}
