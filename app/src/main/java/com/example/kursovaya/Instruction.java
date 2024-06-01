package com.example.kursovaya;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.kursovaya.databinding.ActivityInstructionBinding;

public class Instruction extends SideMenuController {

    private ActivityInstructionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInstructionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SideMenuController.setupSideMenu(this);

        ImageView catHead = findViewById(R.id.catHead);
        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.headrotation);
        catHead.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove the listener to avoid multiple calls
                catHead.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Calculate the pivot point (bottom center) relative to the ImageView
                float pivotX = catHead.getWidth() / 2f;
                float pivotY = catHead.getHeight();

                // Set the pivot point
                catHead.setPivotX(pivotX);
                catHead.setPivotY(pivotY);
            }
        });

        animator.setTarget(catHead);
        animator.start();

    }

}