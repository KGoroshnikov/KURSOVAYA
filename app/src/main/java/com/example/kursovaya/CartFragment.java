package com.example.kursovaya;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCart;
    private CartProductAdapter cartProductAdapter;
    private List<Product> productList;

    private TextView textTotalPrice;
    private Button buttonOrder;

    public static CartFragment newInstance() {
        return new CartFragment();
    }
    public CartFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Инициализация RecyclerView
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Получение списка продуктов из корзины (предполагается, что он уже заполнен)
        productList = ((ShopMainPage) requireActivity()).getCart().getProductList();

        // Инициализация и установка адаптера
        cartProductAdapter = new CartProductAdapter(productList, this);
        recyclerViewCart.setAdapter(cartProductAdapter);

        textTotalPrice = view.findViewById(R.id.text_total_price);
        buttonOrder = view.findViewById(R.id.button_order);
        updateSum();

        buttonCheck();

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ваш код для обработки заказа
                PurchaseConfirmationDialog.show(getContext(), "Ваш заказ подтвержден! Наша команда уже готовит его для вас. Спасибо, что выбрали нас! Ожидайте вашу посылку — она уже в пути к вам.");
            }
        });

        return view;
    }
    public void updateSum(){
        float sum = 0;
        for (Product product : productList){
            sum += product.getPriceInt() * product.getQuantity();
        }
        textTotalPrice.setText(String.valueOf(sum) + " руб.");
    }

    public void productRemoved(){
        ((ShopMainPage) requireActivity()).productRemoved();

        buttonCheck();
    }

    void buttonCheck(){
        if (productList.size() > 0) {
            buttonOrder.setEnabled(true);
            buttonOrder.setAlpha(1);
        }
        else {
            buttonOrder.setEnabled(false);
            buttonOrder.setAlpha(0.5f);
        }
    }

    public void updateCart(){
        cartProductAdapter.updateCart(((ShopMainPage) requireActivity()).getCart().getProductList());
        updateSum();

        buttonCheck();
    }
}
