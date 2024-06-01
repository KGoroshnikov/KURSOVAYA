package com.example.kursovaya;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kursovaya.databinding.ActivityMainBinding;
import com.example.kursovaya.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Находим кнопки входа и регистрации
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        // Устанавливаем обработчики нажатия на кнопки
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(MainActivity.this, ShopMainPage.class);
            startActivity(intent);
            finish();
        }

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

    // Метод для отображения диалогового окна входа
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вход");

        // Надуваем макет для диалогового окна
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(dialogView);

        // Находим поля ввода в диалоговом окне
        EditText emailEditText = dialogView.findViewById(R.id.email_edit_text);
        EditText passwordEditText = dialogView.findViewById(R.id.password_edit_text);

        // Устанавливаем кнопки "Отмена" и "Подтвердить"
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Snackbar.make(binding.getRoot(), "Enter name!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6){
                    Snackbar.make(binding.getRoot(), "Password too short!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent intent = new Intent(MainActivity.this, ShopMainPage.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(binding.getRoot(),
                                        e.getMessage(),
                                        Snackbar.LENGTH_SHORT).show();;
                            }
                        });
            }
        });

        // Отображаем диалоговое окно
        builder.create().show();
    }

    // Метод для отображения диалогового окна регистрации
    private void showRegisterDialog() {
        // Аналогично методу showLoginDialog(), но используем макет dialog_register.xml
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Регистрация");

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_register, null);
        builder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.name_edit_text);
        EditText emailEditText = dialogView.findViewById(R.id.email_edit_text);
        EditText passwordEditText = dialogView.findViewById(R.id.password_edit_text);

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (TextUtils.isEmpty(name)){
                    Snackbar.make(binding.getRoot(), "Enter name!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    Snackbar.make(binding.getRoot(), "Enter email!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Snackbar.make(binding.getRoot(), "Enter password!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6){
                    Snackbar.make(binding.getRoot(), "Password too short!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email,
                                                    password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email);
                                user.setName(name);
                                user.setPass(password);

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent = new Intent(MainActivity.this, ShopMainPage.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }
                        });

            }
        });

        builder.create().show();
    }
}
