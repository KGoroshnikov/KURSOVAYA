package com.example.kursovaya;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.core.view.GravityCompat;


import com.example.kursovaya.databinding.ActivityShopMainPageBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ShopMainPage extends SideMenuController {

    private DrawerLayout drawerLayout;
    private ActivityShopMainPageBinding binding;

    public Cart cartSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopMainPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FragmentStateAdapter pageAdapter = new MyAdapter(this);
        binding.pager.setAdapter(pageAdapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabLayout,
                binding.pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                if (i == 0) tab.setText("Магазин");
                else if (i == 1) tab.setText("Корзина");
            }
        });
        tabLayoutMediator.attach();

        SideMenuController.setupSideMenu(this);

        cartSave = (Cart) getIntent().getSerializableExtra("cartSave");
    }

    public Cart getCart(){
        MyAdapter adapter = (MyAdapter) binding.pager.getAdapter();
        return adapter.shopPage.getCart();
    }
    public void updateCart(){
        MyAdapter adapter = (MyAdapter) binding.pager.getAdapter();
        if (adapter != null && adapter.cartFragment != null) adapter.cartFragment.updateCart();
    }
    public void productRemoved(){
        MyAdapter adapter = (MyAdapter) binding.pager.getAdapter();
        adapter.shopPage.productRemoved();
    }
}