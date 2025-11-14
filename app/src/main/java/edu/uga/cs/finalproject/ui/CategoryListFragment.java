package edu.uga.cs.finalproject.ui;

/*

 - Loads categories (placeholder list for now)
 - Clicking a category navigates to ItemListFragment with categoryId/name
 - FloatingActionButton to add a new category
*/

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
import edu.uga.cs.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.uga.cs.finalproject.adapters.CategoryAdapter;
import edu.uga.cs.finalproject.models.Category;

import java.util.ArrayList;
import java.util.List;

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

    private void loadCategories(){
        // TODO: Replace placeholder data with Firestore query:
        categories.clear();
        Category c1 = new Category(); c1.id = "transport"; c1.name = "Transportation";
        Category c2 = new Category(); c2.id = "household"; c2.name = "Household";
        Category c3 = new Category(); c3.id = "clothing"; c3.name = "Clothing";
        categories.add(c1); categories.add(c2); categories.add(c3);
        adapter.notifyDataSetChanged();
    }
}
