package com.example.kursovaya;

import java.io.Serializable;

public class Product implements Serializable {

    private String name;
    private String price;
    private int price_int;
    private int quantity; // Добавляем поле для хранения количества
    private String imageURL;
    private String description;
    private boolean inCart;
    public Product() {
    }
    public Product(String name, int price, String image, String description) {
        this.description = description;
        this.name = name;
        this.price = String.valueOf(price) + " руб.";
        this.imageURL = image;
        this.price_int = price;
        this.quantity = 1; // Изначально количество товара равно 0
        this.inCart = false;
    }
    public void setInCart(boolean incart){
        this.inCart = incart;
    }
    public boolean getInCart(){
        return inCart;
    }
    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
    public int getPriceInt() {
        return price_int;
    }
    public void setPriceInt(int price_int) {this.price_int = price_int;}
    public void setImage(String imageUrl){
        this.imageURL = imageUrl;
    }
    public String getImage() {
        return imageURL;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
