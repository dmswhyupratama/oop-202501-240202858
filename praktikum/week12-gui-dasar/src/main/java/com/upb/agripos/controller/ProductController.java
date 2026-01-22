package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductFormView;

import javafx.scene.control.Alert;

public class ProductController {
    private ProductFormView view;
    private ProductService service;

    public ProductController(ProductFormView view, ProductService service) {
        this.view = view;
        this.service = service;
        
        // Mendaftarkan Event Handler
        this.view.getBtnAdd().setOnAction(e -> addProduct());
        
        // Load data awal (opsional)
        refreshList();
    }

    private void addProduct() {
        try {
            // 1. Ambil data dari View
            String code = view.getTxtCode().getText();
            String name = view.getTxtName().getText();
            double price = Double.parseDouble(view.getTxtPrice().getText());
            int stock = Integer.parseInt(view.getTxtStock().getText());

            // 2. Bungkus ke Model
            Product p = new Product(code, name, price, stock);

            // 3. Panggil Service (Backend)
            service.insert(p);

            // 4. Update View (Feedback ke user)
            refreshList();
            clearForm();
            showAlert("Sukses", "Produk berhasil ditambahkan!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Harga dan Stok harus angka!");
        } catch (Exception e) {
            showAlert("Error", "Gagal menyimpan: " + e.getMessage());
        }
    }

    private void refreshList() {
        view.getListView().getItems().clear();
        for (Product p : service.getAllProducts()) {
            view.getListView().getItems().add(p.getCode() + " - " + p.getName());
        }
    }

    private void clearForm() {
        view.getTxtCode().clear();
        view.getTxtName().clear();
        view.getTxtPrice().clear();
        view.getTxtStock().clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}