package com.example.paintit;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter{
    public ViewPagerAdapter (@NonNull FragmentActivity fragmentActivity) { super(fragmentActivity); }
    @NonNull
    @Override
    public Fragment createFragment (int position) {
        if (position == 0) {
            return new Page1();
        }
        return new Page2();
    }
    @Override
    public int getItemCount() { return 2; }
}
