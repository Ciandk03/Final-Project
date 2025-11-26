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

public class PendingFragment extends Fragment {

    private RecyclerView recycler;
    private TextView emptyView;
    private TransactionAdapter adapter;
    private List<Transaction> transactions = new ArrayList<>();
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    public PendingFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recycler = view.findViewById(R.id.pendingRecycler);
        emptyView = view.findViewById(R.id.emptyView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TransactionAdapter(transactions, currentUser != null ? currentUser.getUid() : "",
                this::confirmTransaction);
        recycler.setAdapter(adapter);

        if (currentUser != null) {
            loadTransactions();
        }
    }

    private void loadTransactions() {
        mDatabase.child("transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transactions.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Transaction t = child.getValue(Transaction.class);
                    if (t != null && "PENDING".equals(t.getStatus())) {
                        if (currentUser.getUid().equals(t.getBuyerId())
                                || currentUser.getUid().equals(t.getSellerId())) {
                            transactions.add(t);
                        }
                    }
                }
                Collections.sort(transactions, (o1, o2) -> Long.compare(o2.getInitiatedAt(), o1.getInitiatedAt()));

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

    private void confirmTransaction(Transaction t) {
        boolean isSeller = currentUser.getUid().equals(t.getSellerId());
        if (isSeller) {
            t.setSellerConfirmed(true);
        } else {
            t.setBuyerConfirmed(true);
        }

        if (t.isSellerConfirmed() && t.isBuyerConfirmed()) {
            t.setStatus("COMPLETED");
            t.setCompletedAt(System.currentTimeMillis());

            // Also update item status to SOLD if needed, but ItemDetailFragment sets it to
            // PENDING initially.
            // We can set it to SOLD here.
            mDatabase.child("items").child(t.getItemId()).child("status").setValue("SOLD");
        }

        mDatabase.child("transactions").child(t.getId()).setValue(t)
                .addOnSuccessListener(
                        aVoid -> Toast.makeText(getContext(), "Transaction updated", Toast.LENGTH_SHORT).show());
    }
}
