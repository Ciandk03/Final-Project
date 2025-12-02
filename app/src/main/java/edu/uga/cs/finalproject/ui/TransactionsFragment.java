package edu.uga.cs.finalproject.ui;

/*
 - Contains TabLayout + ViewPager2 with Pending (buys/sales) & Completed
*/

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import edu.uga.cs.finalproject.R;

public class TransactionsFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 pager;

    public TransactionsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.transTabLayout);
        pager = view.findViewById(R.id.transViewPager);

        pager.setAdapter(new TransactionsPagerAdapter(this));
        new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            if(position==0) tab.setText("Pending");
            else tab.setText("Completed");
        }).attach();

        if (savedInstanceState != null) {
            int savedPage = savedInstanceState.getInt("savedPage", 0);
            pager.setCurrentItem(savedPage, false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (pager != null) {
            outState.putInt("savedPage", pager.getCurrentItem());
        }
    }
}

