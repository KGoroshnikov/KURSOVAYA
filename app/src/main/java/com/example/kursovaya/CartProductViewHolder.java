package com.example.kursovaya;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartProductViewHolder extends RecyclerView.ViewHolder {

    TextView productName, productPrice, productQuantity;
    Button buttonIncreaseQuantity, buttonDecreaseQuantity;

    public CartProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.text_cart_product_name);
        productPrice = itemView.findViewById(R.id.text_cart_product_price);
        productQuantity = itemView.findViewById(R.id.text_quantity);
        buttonIncreaseQuantity = itemView.findViewById(R.id.button_increase_quantity);
        buttonDecreaseQuantity = itemView.findViewById(R.id.button_decrease_quantity);
    }
}
