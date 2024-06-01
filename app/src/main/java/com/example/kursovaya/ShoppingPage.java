package com.example.kursovaya;

import android.app.Application;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingPage extends Fragment implements OnAddToCartListener {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private Cart cart; // Объект корзины
    private EditText editTextSearch; // Поисковая строка
    private DatabaseReference databaseReference;

    private int pageNumber;
    public static ShoppingPage newInstance() {
        return new ShoppingPage();
    }
    public ShoppingPage() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_item_product, container, false);

        ShopMainPage activity = (ShopMainPage) getActivity();
        if (activity.cartSave != null) cart = activity.cartSave;
        else cart = new Cart();

        // Инициализация RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        editTextSearch = view.findViewById(R.id.edit_text_search);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase().trim();
                filterProducts(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Заполнение списка товаров
        productList = new ArrayList<>();
        productList.add(new Product("Название товара 1", 10, "null", ""));
        // Добавьте остальные товары при необходимости

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        boolean addedFromCart = false;
                        for(int p = 0; p < cart.getProductList().size(); p++){
                            if (Objects.equals(cart.getProductList().get(p).getName(), product.getName())){
                                productList.add(cart.getProductList().get(p));
                                addedFromCart = true;
                                break;
                            }
                        }
                        if (!addedFromCart) productList.add(product);

                        productAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при получении данных из базы данных
            }
        });

        // Инициализация и установка адаптера
        productAdapter = new ProductAdapter(productList, this, getContext(), this);
        recyclerView.setAdapter(productAdapter);

        return view;
    }

    public void productRemoved(){
        productAdapter.productRemoved();
    }

    @Override
    public void onAddToCart(Product product) {
        if (cart.getProductList().contains(product)) return;
        product.setInCart(true);
        cart.addProduct(product);
        ((ShopMainPage) getActivity()).updateCart();
    }

    public Cart getCart(){
        return cart;
    }

    private void filterProducts(String searchText) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(searchText)) {
                filteredList.add(product);
            }
        }
        productAdapter.updateList(filteredList);
    }
}
