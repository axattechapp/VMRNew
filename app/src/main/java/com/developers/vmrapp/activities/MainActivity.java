package com.developers.vmrapp.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.developers.vmrapp.R;
import com.developers.vmrapp.Util;
import com.developers.vmrapp.activities.auth.ChooseActivity;
import com.developers.vmrapp.activities.auth.LoginActivity;
import com.developers.vmrapp.databinding.ActivityMainBinding;
import com.developers.vmrapp.fragments.main.HomeFragment;
import com.developers.vmrapp.fragments.main.MarketFragment;
import com.developers.vmrapp.fragments.main.ProfileFragment;
import com.developers.vmrapp.fragments.main.SettingsFragment;
import com.developers.vmrapp.fragments.main.ShopFragment;
import com.developers.vmrapp.fragments.privacy.PrivacyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private NavController navController;

    private NavHostFragment navHostFragment;

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding
                .inflate(getLayoutInflater());

        Util.keyhashes(this);

        binding.mainNavigation.setOnNavigationItemSelectedListener(navListener);


        setContentView(binding.getRoot());

//        SharedPreferences preferences=getSharedPreferences("my_token",MODE_PRIVATE);
//        String token=preferences.getString("access_token","");
//        if (token.equals(""))
//        {
//            startActivity(new Intent(this, ChooseActivity.class));
//        }

        setupBottomNavigation();

    }

    private void setupBottomNavigation() {
        PrivacyFragment fragment=new PrivacyFragment();

        navHostFragment = (NavHostFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.main_container);

        if (navHostFragment != null) {

            navController = navHostFragment.getNavController();

            NavigationUI.setupWithNavController(
                    binding.mainNavigation, navController);


        }else
        {
            Toast.makeText(MainActivity.this, "Null", Toast.LENGTH_SHORT).show();
        }
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            //Here I want to call Fragment in if condition
            Log.e("des","d"+destination.getId());


            if (destination.getId()==2131296841)
            {
//                hideBottomNav();
                Log.w("ABC", "A");
            }
             else if (destination.getId()==2131296841)
            {
                hideBottomNav();
                Log.w("ABC", "c");
            }else
            {
                showBottomNav();
                Log.w("ABC", "abc");
            }


        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.navigation_market:
                            selectedFragment = new MarketFragment();
                            break;
                        case R.id.navigation_shop:
                            selectedFragment = new ShopFragment();
                            break;
                        case R.id.navigation_setting:
                            selectedFragment = new SettingsFragment();
                            break;
                        case R.id.navigation_profile:
                            selectedFragment = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            selectedFragment).addToBackStack(null).commit();

                    return true;
                }


            };
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
             getSupportFragmentManager().popBackStackImmediate();
        }
        else
            super.onBackPressed();
    }

    private void showBottomNav() {
        binding.mainNavigation.setVisibility(View.VISIBLE);

    }

    private void hideBottomNav() {
        binding.mainNavigation.setVisibility(View.INVISIBLE);

    }


}