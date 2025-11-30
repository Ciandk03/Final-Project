package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MyItemsFragment extends Fragment {

    private RecyclerView recycler;
    private TextView emptyView;
    private ItemAdapter adapter;
    private List<Item> items = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    public MyItemsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference("items");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recycler = view.findViewById(R.id.myItemsRecycler);
        emptyView = view.findViewById(R.id.emptyView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ItemAdapter(items, currentUser.getUid(), new ItemAdapter.theOnItemAction() {
            @Override
            public void onClick(Item item) {
                Bundle b = new Bundle();
                b.putString("itemId", item.getId());
                Navigation.findNavController(view).navigate(R.id.action_global_itemDetailFragment, b);
            }

            @Override
            public void onDelete(Item item) {
                // Delete the item from Firebase
                if (item.getId() != null) {
                    mDatabase.child(item.getId()).removeValue()
                            .addOnSuccessListener(aVoid -> Toast
                                    .makeText(getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast
                                    .makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show());
                }
            }
        });
        recycler.setAdapter(adapter);

        if (currentUser != null) {
            loadMyItems();
        } else {
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("Please login to view your items.");
        }
    }

    private void loadMyItems() {
        Query query = mDatabase.orderByChild("sellerId").equalTo(currentUser.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Item item = child.getValue(Item.class);
                    if (item != null) {
                        items.add(item);
                    }
                }
                // Sort by date descending
                Collections.sort(items, (o1, o2) -> Long.compare(o2.getCreatedAt(), o1.getCreatedAt()));

                if (items.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
