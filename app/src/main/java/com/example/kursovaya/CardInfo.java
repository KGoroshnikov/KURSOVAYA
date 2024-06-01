package com.example.kursovaya;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.example.kursovaya.databinding.ActivityCardInfoBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CardInfo extends AppCompatActivity {

    private ActivityCardInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Product product = (Product) getIntent().getSerializableExtra("product");
        Cart cartSave = (Cart) getIntent().getSerializableExtra("cart");
        Picasso.get().load(product.getImage()).into(binding.productImageView);
        binding.productNameTextView.setText(product.getName());
        binding.productDescriptionTextView.setText(product.getDescription());
        binding.productPriceTextView.setText(product.getPriceInt() + " руб.");

        for(int i = 0; i < cartSave.getProductList().size(); i++){
            if (Objects.equals(cartSave.getProductList().get(i).getName(), product.getName()) &&
                    cartSave.getProductList().get(i).getInCart()){
                binding.addToCartButton.setText("В корзине!");
                binding.addToCartButton.setAlpha(0.5f);
            }
        }
        binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addToCartButton.setText("В корзине!");
                // Убираем возможность нажатия на кнопку
                binding.addToCartButton.setAlpha(0.5f);
                for(int i = 0; i < cartSave.getProductList().size(); i++){
                    if (Objects.equals(cartSave.getProductList().get(i).getName(), product.getName()) &&
                            cartSave.getProductList().get(i).getInCart()){
                        return;
                    }
                }
                product.setInCart(true);
                cartSave.addProduct(product);
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardInfo.this, ShopMainPage.class);
                intent.putExtra("cartSave", cartSave);
                startActivity(intent);
            }
        });
    }
}