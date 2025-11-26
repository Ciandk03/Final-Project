package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.adapters.ItemAdapter;
import edu.uga.cs.finalproject.models.Item;

public class ItemListFragment extends Fragment {

    private RecyclerView recycler;
    private ItemAdapter adapter;
    private List<Item> items = new ArrayList<>();
    private String categoryId, categoryName;
    private DatabaseReference mDatabase;

    public ItemListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            categoryName = getArguments().getString("categoryName");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("items");

        recycler = view.findViewById(R.id.itemRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(items, item -> {
            Bundle b = new Bundle();
            b.putString("itemId", item.getId());
            Navigation.findNavController(view).navigate(R.id.action_itemList_to_itemDetail, b);
        });
        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.addItemFab);
        fab.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("categoryId", categoryId);
            b.putString("categoryName", categoryName);
            Navigation.findNavController(v).navigate(R.id.action_itemList_to_addItem, b);
        });

        loadItems();
    }

    private void loadItems() {
        Query query = mDatabase.orderByChild("categoryId").equalTo(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    // Filter by status if needed, e.g., only "AVAILABLE"
                    if (item != null && "AVAILABLE".equals(item.getStatus())) {
                        items.add(item);
                    }
                }
                // Sort by date descending (Newest first)
                Collections.sort(items, (o1, o2) -> Long.compare(o2.getCreatedAt(), o1.getCreatedAt()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
