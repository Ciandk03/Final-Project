package edu.uga.cs.finalproject.ui;

/*
 ItemListFragment
 - Shows items for a given categoryId (argument)
 - FloatingActionButton opens AddItemFragment
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.adapters.ItemAdapter;
import edu.uga.cs.finalproject.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragment extends Fragment {

    private RecyclerView recycler;
    private ItemAdapter adapter;
    private List<Item> items = new ArrayList<>();
    private String categoryId, categoryName;

    public ItemListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        if(getArguments()!=null){
            categoryId = getArguments().getString("categoryId");
            categoryName = getArguments().getString("categoryName");
        }

        recycler = view.findViewById(R.id.itemRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(items, item -> {
            Bundle b = new Bundle();
            b.putString("itemId", item.id);
            Navigation.findNavController(view).navigate(R.id.action_itemList_to_itemDetail, b);
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.addItemFab);
        fab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_itemList_to_addItem));

        loadItems();
    }

    private void loadItems(){
        // TODO: Load items from Firestore where categoryId == this.categoryId and status == "active"
        items.clear();
        // placeholder sample
        Item it = new Item(); it.id = "item1"; it.title = "Bike"; it.description = "Good bike"; it.isFree = false; it.priceCents = 20000;
        items.add(it);
        adapter.notifyDataSetChanged();
    }
}
