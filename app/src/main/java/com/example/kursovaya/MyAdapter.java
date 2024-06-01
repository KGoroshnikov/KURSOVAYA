package com.example.kursovaya;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyAdapter extends FragmentStateAdapter {
    public ShoppingPage shopPage;
    public CartFragment cartFragment;
    public MyAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            Fragment shopPageFrag = ShoppingPage.newInstance();
            shopPage = (ShoppingPage)shopPageFrag;
            return shopPageFrag;
        }

        Fragment cartFragmentFrag = CartFragment.newInstance();
        cartFragment = (CartFragment)cartFragmentFrag;
        return cartFragmentFrag;
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
