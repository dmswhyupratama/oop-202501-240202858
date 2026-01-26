package com.upb.agripos.controller;

import com.upb.agripos.dao.ReturnDAO;
import com.upb.agripos.dao.TransactionDAOImpl;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.view.ReturnView;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;

public class ReturnController {
    private ReturnView view;
    private TransactionDAOImpl transactionDAO;
    private ReturnDAO returnDAO;

    public ReturnController(ReturnView view) {
        this.view = view;
        this.transactionDAO = new TransactionDAOImpl();
        this.returnDAO = new ReturnDAO();
        initController();
    }

    private void initController() {
        // 1. Event Cari Transaksi
        view.getBtnSearch().setOnAction(e -> searchTransaction());

        // 2. Event Klik Tabel (Enable button)
        view.getTable().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = (newVal != null);
            view.getBtnProcessReturn().setDisable(!hasSelection);
        });

        // 3. Event Proses Retur
        view.getBtnProcessReturn().setOnAction(e -> processReturn());
    }

    private void searchTransaction() {
        String trxId = view.getTxtTrxId().getText().trim();
        if (trxId.isEmpty()) {
            showAlert("Error", "Masukkan ID Transaksi dulu!");
            return;
        }

        // Ambil data dari DAO
        var items = transactionDAO.getTransactionItems(trxId);
        
        if (items.isEmpty()) {
            showAlert("Tidak Ditemukan", "ID Transaksi tidak ditemukan atau tidak ada barang.");
            view.getTable().setItems(FXCollections.observableArrayList());
        } else {
            view.getTable().setItems(FXCollections.observableArrayList(items));
        }
    }

    private void processReturn() {
        // Ambil item yang dipilih
        CartItem selectedItem = view.getTable().getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        String qtyStr = view.getTxtReturnQty().getText().trim();
        String reason = view.getTxtReason().getText().trim();

        if (qtyStr.isEmpty() || reason.isEmpty()) {
            showAlert("Error", "Jumlah dan Alasan wajib diisi!");
            return;
        }

        try {
            int qtyReturn = Integer.parseInt(qtyStr);
            
            // Validasi: Retur tidak boleh <= 0
            if (qtyReturn <= 0) {
                showAlert("Error", "Jumlah retur minimal 1.");
                return;
            }

            // Validasi: Retur tidak boleh melebihi pembelian
            if (qtyReturn > selectedItem.getQuantity()) {
                showAlert("Error", "Jumlah retur melebihi jumlah pembelian! (Maks: " + selectedItem.getQuantity() + ")");
                return;
            }

            // PROSES RETUR KE DATABASE
            returnDAO.processReturn(
                view.getTxtTrxId().getText().trim(),
                selectedItem.getProduct().getCode(),
                qtyReturn,
                reason
            );

            showAlert("Sukses", "Retur berhasil diproses!\nStok barang telah dikembalikan.");
            
            // Reset Form
            view.getTxtReturnQty().clear();
            view.getTxtReason().clear();
            searchTransaction(); // Refresh tabel

        } catch (NumberFormatException e) {
            showAlert("Error", "Jumlah harus angka valid!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal", "Error database: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}