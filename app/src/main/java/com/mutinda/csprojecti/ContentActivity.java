package com.mutinda.csprojecti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mutinda.csprojecti.NavigationBar.AccountProfileFragment;
import com.mutinda.csprojecti.NavigationBar.ListingsFragment;
import com.mutinda.csprojecti.NavigationBar.MapsFragment;
import com.mutinda.csprojecti.NavigationBar.SettingsFragment;

public class ContentActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        navigationView = findViewById(R.id.bottom_nav);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.map_item:
                        fragment = new MapsFragment();
                        break;
                    case R.id.account_item:
                        fragment = new AccountProfileFragment();
                        break;
                    case R.id.settings_item:
                       fragment = new SettingsFragment();
                        break;
                    default:
                        fragment = new ListingsFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView3, fragment).commit();
                return true;
            }
        });
    }
}