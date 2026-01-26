package com.upb.agripos.model;

public class CartItem {
    private Product product;
    private int quantity;
    private double subtotal;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.subtotal = product.getPrice() * quantity;
    }

    public void addQuantity(int qty) {
        this.quantity += qty;
        this.subtotal = this.product.getPrice() * this.quantity;
    }

    // Getter
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return subtotal; }
    
    // Helper untuk tampilan tabel
    public String getProductName() { return product.getName(); }
    public double getPrice() { return product.getPrice(); }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.product.getPrice() * this.quantity;
    }
}