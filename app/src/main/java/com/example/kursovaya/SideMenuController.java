package com.example.kursovaya;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.example.kursovaya.databinding.ActivityShopMainPageBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public abstract class SideMenuController extends AppCompatActivity {

    protected static DrawerLayout drawerLayout;

    private static AppCompatActivity activity;

    public static void setupSideMenu(AppCompatActivity activity0) {
        activity = activity0;
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Candy Shop");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        drawerLayout = activity.findViewById(R.id.drawer_layout);

        NavigationView navigationView = activity.findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.textView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    textView.setText(name);
                } else {
                    textView.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                textView.setText("");
            }
        });

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.nav_author) {
                            openAuthorActivity(activity);
                        } else if (id == R.id.nav_home) {
                            openMainActivity(activity);
                        }
                        else if (id == R.id.nav_programm) {
                            openProgramActivity(activity);
                        }
                        else if (id == R.id.nav_instruction) {
                            openInstructionActivity(activity);
                        }
                        else if (id == R.id.nav_exit) {
                            userExit(activity);
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser.getEmail().contains("admin")){
            menu.add(Menu.NONE, 123, Menu.NONE, "АДМИН")
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        if (item.getItemId() == 123) {
            Intent intent = new Intent(activity, NewProductActivity.class);
            activity.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static void userExit(Context context) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private static void openAuthorActivity(Context context) {
        Intent intent = new Intent(context, AboutAuthor.class);
        context.startActivity(intent);
    }

    private static void openMainActivity(Context context) {
        Intent intent = new Intent(context, ShopMainPage.class);
        context.startActivity(intent);
    }

    private static void openProgramActivity(Context context) {
        Intent intent = new Intent(context, AboutProgram.class);
        context.startActivity(intent);
    }

    private static void openInstructionActivity(Context context) {
        Intent intent = new Intent(context, Instruction.class);
        context.startActivity(intent);
    }

}
