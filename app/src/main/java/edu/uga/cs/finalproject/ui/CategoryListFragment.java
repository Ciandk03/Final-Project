
package edu.uga.cs.finalproject.ui;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.adapters.CategoryAdapter;
import edu.uga.cs.finalproject.models.Category;

public class CategoryListFragment extends Fragment {

    private RecyclerView recycler;
    private CategoryAdapter adapter;
    private List<Category> categories = new ArrayList<>();

    public CategoryListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        recycler = view.findViewById(R.id.categoryRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CategoryAdapter(categories, category -> {
            // on click -> navigate to ItemListFragment with args
            Bundle b = new Bundle();
            b.putString("categoryId", category.id);
            b.putString("categoryName", category.name);
            Navigation.findNavController(view).navigate(R.id.action_category_to_itemList, b);
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.addCategoryFab);
        fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_category_to_addCategory));

        loadCategories();
    }

    private void loadCategories() {
        FirebaseDatabase.getInstance().getReference("categories")
                .orderByChild("name") // ✅ ensures alphabetical order
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        categories.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Category theC = child.getValue(Category.class);
                            if (theC != null) {
                                categories.add(theC);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Optionally log or show an error message
                    }
                });
    }
}
