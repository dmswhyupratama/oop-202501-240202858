package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductTableView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class ProductController {
    private ProductTableView view;
    private ProductService service;

    public ProductController(ProductTableView view, ProductService service) {
        this.view = view;
        this.service = service;

        // Event Handler Tambah (Lambda)
        this.view.getBtnAdd().setOnAction(e -> addProduct());

        // Event Handler Hapus (Lambda) - REQUIREMENT WEEK 13
        this.view.getBtnDelete().setOnAction(e -> deleteProduct());

        // Load data awal
        loadData();
    }

    private void addProduct() {
        try {
            Product p = new Product(
                view.getTxtCode().getText(),
                view.getTxtName().getText(),
                Double.parseDouble(view.getTxtPrice().getText()),
                Integer.parseInt(view.getTxtStock().getText())
            );
            service.insert(p);
            loadData(); // Refresh tabel
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil disimpan!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void deleteProduct() {
        // Ambil item yang sedang dipilih di tabel
        Product selected = view.getTable().getSelectionModel().getSelectedItem();
        
        if (selected != null) {
            // Panggil service delete
            service.delete(selected.getCode());
            // Refresh tabel
            loadData();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil dihapus!");
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih produk yang mau dihapus dulu!");
        }
    }

    private void loadData() {
        // Ambil list dari service, ubah jadi ObservableList buat JavaFX
        ObservableList<Product> products = FXCollections.observableArrayList(service.getAllProducts());
        view.getTable().setItems(products);
    }

    private void clearForm() {
        view.getTxtCode().clear();
        view.getTxtName().clear();
        view.getTxtPrice().clear();
        view.getTxtStock().clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}