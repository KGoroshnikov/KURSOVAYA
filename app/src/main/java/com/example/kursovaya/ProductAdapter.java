package com.example.kursovaya;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnAddToCartListener addToCartListener;
    private Context context;
    private ShoppingPage shoppingPage;

    public ProductAdapter(List<Product> productList, OnAddToCartListener addToCartListener, Context context, ShoppingPage shoppingPage) {
        this.context = context;
        this.productList = productList;
        this.addToCartListener = addToCartListener;
        this.shoppingPage = shoppingPage;
    }

    public void updateList(List<Product> newproductList) {
        this.productList = newproductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        Picasso.get().load(product.getImage()).into(holder.productImage);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CardInfo.class);

                intent.putExtra("product", product);
                intent.putExtra("cart", shoppingPage.getCart());
                context.startActivity(intent);
            }
        });

        // Обработка нажатия на кнопку добавления в корзину
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Изменяем текст кнопки на "В корзине"
                holder.addToCartButton.setText("В корзине!");
                holder.addToCartButton.setAlpha(0.5f);
                addToCartListener.onAddToCart(product);
            }
        });

        Log.d(String.valueOf(product.getInCart()), "1123");

        if (product.getInCart()){
            holder.addToCartButton.setText("В корзине!");
            holder.addToCartButton.setAlpha(0.5f);
        }
        else{
            holder.addToCartButton.setText("В корзину");
            holder.addToCartButton.setAlpha(1);
        }
    }

    public void productRemoved(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;

        ImageView productImage;
        TextView productName, productPrice;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            productImage = itemView.findViewById(R.id.image_product);
            productName = itemView.findViewById(R.id.text_product_name);
            productPrice = itemView.findViewById(R.id.text_product_price);
            addToCartButton = itemView.findViewById(R.id.button_add_to_cart);
        }
    }
}
