package com.example.kursovaya;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursovaya.databinding.ActivityAboutAuthorBinding;
import com.example.kursovaya.databinding.ActivityAboutProgramBinding;

public class AboutProgram extends SideMenuController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityAboutProgramBinding binding = ActivityAboutProgramBinding.inflate(getLayoutInflater());
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