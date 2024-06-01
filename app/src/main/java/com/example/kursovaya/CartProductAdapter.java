package com.example.kursovaya;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductViewHolder> {

    private List<Product> productList;
    private CartFragment cartFragment;

    public CartProductAdapter(List<Product> productList, CartFragment cartFragment) {
        this.cartFragment = cartFragment;
        this.productList = productList;
    }

    public void updateCart(List<Product> newproductList) {
        this.productList = newproductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cart_product, parent, false);
        return new CartProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));

        // Обработчик нажатия на кнопку увеличения количества товара
        holder.buttonIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = product.getQuantity() + 1;
                product.setQuantity(newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
                cartFragment.updateSum();
            }
        });

        // Обработчик нажатия на кнопку уменьшения количества товара
        holder.buttonDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = product.getQuantity() - 1;
                if (newQuantity > 0) {
                    product.setQuantity(newQuantity);
                    holder.productQuantity.setText(String.valueOf(newQuantity));
                    cartFragment.updateSum();
                }
                else if (newQuantity <= 0){
                    product.setInCart(false);
                    productList.remove(product);
                    notifyDataSetChanged();
                    cartFragment.updateSum();
                    cartFragment.productRemoved();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
