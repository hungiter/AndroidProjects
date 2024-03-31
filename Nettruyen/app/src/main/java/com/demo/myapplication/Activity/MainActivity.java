package com.demo.myapplication.Activity;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.demo.myapplication.Fragment.HomeFragment;
import com.demo.myapplication.Fragment.LikeFragment;
import com.demo.myapplication.Fragment.OfflineFragment;
import com.demo.myapplication.R;
import com.demo.myapplication.Fragment.SettingsFragment;
import com.demo.myapplication.databinding.ActivityMainBinding;
import com.demo.myapplication.databinding.ActivityRegisterBinding;
import com.demo.myapplication.model.AppRepo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment activeFragment;
    final HomeFragment homeFragment = new HomeFragment();
    final LikeFragment likeFragment = new LikeFragment();
    final OfflineFragment offlineFragment = new OfflineFragment();
    final SettingsFragment settingsFragment = new SettingsFragment();
    FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        initFragment();
        initBottomNav();

    }
    public void initBottomNav() {
        binding.bottomNavigation.setItemIconTintList(null);

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bot_nav_home) {
                addFragment(homeFragment);
            } else if (item.getItemId() == R.id.bot_nav_like) {
                addFragment(likeFragment);
            } else if (item.getItemId() == R.id.bot_nav_offline) {
                addFragment(offlineFragment);
            } else if (item.getItemId() == R.id.bot_nav_settings) {
                addFragment(settingsFragment);
            }
            return true;
        });
    }

    public void addFragment(Fragment frag) {
        fm.beginTransaction().hide(activeFragment).show(frag).commit();
        activeFragment = frag;
    }

    private void initFragment() {
        fm.beginTransaction().add(R.id.frame_container, settingsFragment, "4").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.frame_container, offlineFragment, "3").hide(offlineFragment).commit();
        fm.beginTransaction().add(R.id.frame_container, likeFragment, "2").hide(likeFragment).commit();
        fm.beginTransaction().add(R.id.frame_container, homeFragment, "1").commit();
        activeFragment = homeFragment;
    }
}