package edu.uga.cs.finalproject.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/* Pager with two tabs - Pending & Completed */
public class TransactionsPagerAdapter extends FragmentStateAdapter {

    public TransactionsPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new PendingFragment() : new CompletedFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        // Use position as stable ID
        return position;
    }

    @Override
    public boolean containsItem(long itemId) {
        // We only have two tabs: 0 = Pending, 1 = Completed
        return itemId >= 0 && itemId < getItemCount();
    }
}
