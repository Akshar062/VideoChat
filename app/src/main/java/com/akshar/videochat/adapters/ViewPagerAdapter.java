package com.akshar.videochat.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.akshar.videochat.fregments.HistoryFragment;
import com.akshar.videochat.fregments.HomeFragment;
import com.akshar.videochat.fregments.ContactFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the appropriate fragment based on the position
        switch (position) {
            case 0:
                return new HomeFragment(); // Replace with your actual Fragment class
            case 1:
                return new HistoryFragment();
            case 2:
                return new ContactFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of pages
        return 3; // Adjust based on the number of pages you have
    }
}
