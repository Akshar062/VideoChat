package com.akshar.videochat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.akshar.videochat.adapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvTitle = findViewById(R.id.toolbarTitle);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new ViewPagerAdapter(this));

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.menu_comment) {
                viewPager.setCurrentItem(0, true);
                tvTitle.setText("");
                return true;
            } else if (item.getItemId() == R.id.menu_meetings) {
                viewPager.setCurrentItem(1, true);
                tvTitle.setText("Profile");
                return true;
            } else if (item.getItemId() == R.id.menu_contact) {
                viewPager.setCurrentItem(2, true);
                tvTitle.setText("Profile");
                return true;
            } else if (item.getItemId() == R.id.menu_settings) {
                viewPager.setCurrentItem(3, true);
                tvTitle.setText("Profile");
                return true;
            } else {
                return false;
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_comment).setChecked(true);
                    tvTitle.setText("Meet & Chat");
                } else if (position == 1) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_meetings).setChecked(true);
                    tvTitle.setText("Profile");
                } else if (position == 2) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_contact).setChecked(true);
                    tvTitle.setText("Profile");
                } else if (position == 3) {
                    bottomNavigationView.getMenu().findItem(R.id.menu_settings).setChecked(true);
                    tvTitle.setText("Profile");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        } else {
            super.onBackPressed();
        }
    }
}

