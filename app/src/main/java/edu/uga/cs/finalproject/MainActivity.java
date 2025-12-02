package edu.uga.cs.finalproject;

/*
 - Hosts the NavHostFragment and the BottomNavigationView
 - Navigation graph handles fragment transitions
*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);

        if (savedInstanceState != null) {
            // Use categoryFragment as the default since it's your startDestination
            int selectedItemId = savedInstanceState.getInt("savedSelectedTab", R.id.categoryFragment);
            bottomNav.setSelectedItemId(selectedItemId);
        }

        // Check auth state
        if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() == null) {
            navController.navigate(R.id.loginFragment);
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.loginFragment || destination.getId() == R.id.registerFragment) {
                bottomNav.setVisibility(android.view.View.GONE);
            } else {
                bottomNav.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bottomNav != null) {
            outState.putInt("savedSelectedTab", bottomNav.getSelectedItemId());
        }
    }
}

