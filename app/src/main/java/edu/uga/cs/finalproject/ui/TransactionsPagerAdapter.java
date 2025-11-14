package edu.uga.cs.finalproject.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/* Pager with two tabs - Pending & Completed */
public class TransactionsPagerAdapter extends FragmentStateAdapter {
    public TransactionsPagerAdapter(@NonNull Fragment fragment){ super(fragment); }
    @NonNull @Override public Fragment createFragment(int position){
        return position==0 ? new PendingFragment() : new CompletedFragment();
    }
    @Override public int getItemCount(){ return 2; }
}
