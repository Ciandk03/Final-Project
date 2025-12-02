package edu.uga.cs.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uga.cs.finalproject.R;
import edu.uga.cs.finalproject.adapters.TransactionAdapter;
import edu.uga.cs.finalproject.models.Transaction;

public class CompletedFragment extends Fragment {

    private RecyclerView recycler;
    private TextView emptyView;
    private TransactionAdapter adapter;
    private List<Transaction> transactions = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    public CompletedFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recycler = view.findViewById(R.id.completedRecycler);
        emptyView = view.findViewById(R.id.emptyView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TransactionAdapter(transactions,
                currentUser != null ? currentUser.getUid() : "",
                null);
        recycler.setAdapter(adapter);

        if (savedInstanceState != null) {
            transactions.clear();
            transactions.addAll((List<Transaction>) savedInstanceState.getSerializable("savedTransactions"));
            adapter.notifyDataSetChanged();

            int pos = savedInstanceState.getInt("scrollPosition", 0);
            recycler.scrollToPosition(pos);

            // Handle empty view visibility
            if (transactions.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);
            }
        } else {
            if (currentUser != null) {
                loadTransactions();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("savedTransactions", new ArrayList<>(transactions));
        int pos = ((LinearLayoutManager) recycler.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt("scrollPosition", pos);
    }

    private void loadTransactions() {
        mDatabase.child("transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactions.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Transaction t = child.getValue(Transaction.class);
                    if (t != null && "COMPLETED".equals(t.getStatus())) {
                        if (currentUser.getUid().equals(t.getBuyerId())
                                || currentUser.getUid().equals(t.getSellerId())) {
                            transactions.add(t);
                        }
                    }
                }
                Collections.sort(transactions,
                        (o1, o2) -> Long.compare(o2.getCompletedAt(), o1.getCompletedAt()));

                if (transactions.isEmpty()) {
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
            }
        });
    }
}
