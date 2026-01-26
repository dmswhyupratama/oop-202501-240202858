package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class ProductController {
    private ProductView view;
    private ProductService service;

    public ProductController(ProductView view, ProductService service) {
        this.view = view;
        this.service = service;

        initController();
        loadData();
    }

    private void initController() {
        // Event Tombol Simpan (Create)
        view.getBtnSave().setOnAction(e -> saveProduct());

        // Event Tombol Update (Edit)
        view.getBtnUpdate().setOnAction(e -> updateProduct());

        // Event Tombol Hapus (Delete)
        view.getBtnDelete().setOnAction(e -> deleteProduct());

        // Event Tombol Clear
        view.getBtnClear().setOnAction(e -> clearForm());

        // Event Seleksi Tabel (Untuk mode Edit)
        view.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Populate Form
                view.getTxtCode().setText(newSelection.getCode());
                view.getTxtName().setText(newSelection.getName());
                view.getTxtPrice().setText(String.valueOf((int)newSelection.getPrice())); // Casting int biar rapi
                view.getTxtStock().setText(String.valueOf(newSelection.getStock()));

                // Ubah Mode Tombol
                view.getBtnSave().setDisable(true);    // Gabisa save baru kalau lagi pilih data
                view.getBtnUpdate().setDisable(false); // Enable Update
                view.getBtnDelete().setDisable(false); // Enable Delete
                view.getTxtCode().setDisable(true);    // Kunci Primary Key (Gaboleh diedit)
            }
        });
    }

    private void saveProduct() {
        try {
            Product p = new Product(
                view.getTxtCode().getText(),
                view.getTxtName().getText(),
                Double.parseDouble(view.getTxtPrice().getText()),
                Integer.parseInt(view.getTxtStock().getText())
            );
            service.insert(p);
            loadData();
            clearForm();
            showAlert("Sukses", "Produk berhasil ditambahkan!");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void updateProduct() {
        try {
            // Ambil data dari form
            Product p = new Product(
                view.getTxtCode().getText(), // Kode diambil tapi readonly
                view.getTxtName().getText(),
                Double.parseDouble(view.getTxtPrice().getText()),
                Integer.parseInt(view.getTxtStock().getText())
            );
            
            // Panggil service update
            service.update(p);
            
            loadData();
            clearForm();
            showAlert("Sukses", "Data produk berhasil diperbarui!");
        } catch (Exception e) {
            showAlert("Error", "Gagal update: " + e.getMessage());
        }
    }

    private void deleteProduct() {
        String code = view.getTxtCode().getText();
        if (code.isEmpty()) return;

        service.delete(code);
        loadData();
        clearForm();
        showAlert("Sukses", "Produk berhasil dihapus!");
    }

    private void loadData() {
        ObservableList<Product> products = FXCollections.observableArrayList(service.getAllProducts());
        view.getTable().setItems(products);
    }

    private void clearForm() {
        view.getTxtCode().clear();
        view.getTxtName().clear();
        view.getTxtPrice().clear();
        view.getTxtStock().clear();
        
        // Reset State
        view.getTable().getSelectionModel().clearSelection();
        view.getTxtCode().setDisable(false);   // Buka kunci PK
        view.getBtnSave().setDisable(false);   // Enable Save
        view.getBtnUpdate().setDisable(true);  // Disable Update
        view.getBtnDelete().setDisable(true);  // Disable Delete
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}