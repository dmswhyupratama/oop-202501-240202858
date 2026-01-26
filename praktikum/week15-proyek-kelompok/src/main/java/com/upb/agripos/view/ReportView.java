package com.upb.agripos.view;

import com.upb.agripos.model.Transaction;
import com.upb.agripos.util.AppStyles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ReportView {
    
    private TableView<Transaction> table = new TableView<>();
    private Button btnRefresh = new Button("Refresh Data");
    // [BARU] Tombol Export
    private Button btnExport = new Button("Export CSV"); 

    public Parent asParent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle(AppStyles.CARD_STYLE); // Style lama tetap dipakai

        // 1. HEADER
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT); // Align items vertically center

        Label lblTitle = new Label("Laporan Penjualan");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblTitle.setTextFill(javafx.scene.paint.Color.web(AppStyles.COL_PRIMARY));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Style Tombol Refresh (Lama)
        btnRefresh.setStyle(AppStyles.BTN_SUCCESS);
        
        // [BARU] Style Tombol Export (Warna Oranye biar beda)
        btnExport.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5;");
        
        // Masukkan kedua tombol ke header
        header.getChildren().addAll(lblTitle, spacer, btnRefresh, btnExport);

        // 2. TABEL TRANSAKSI
        setupTableColumns();
        
        VBox.setVgrow(table, Priority.ALWAYS);

        root.getChildren().addAll(header, new Separator(), table);
        return root;
    }

    private void setupTableColumns() {
        TableColumn<Transaction, String> colId = new TableColumn<>("ID Transaksi");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Transaction, String> colDate = new TableColumn<>("Tanggal");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<Transaction, String> colCashier = new TableColumn<>("Kasir");
        colCashier.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        
        TableColumn<Transaction, String> colCust = new TableColumn<>("Pelanggan");
        colCust.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        TableColumn<Transaction, String> colMethod = new TableColumn<>("Metode");
        colMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        
        TableColumn<Transaction, String> colTotal = new TableColumn<>("Total");
        // Tetap pakai formattedTotal sesuai kode lama agar format Rp muncul
        colTotal.setCellValueFactory(new PropertyValueFactory<>("formattedTotal")); 
        colTotal.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");

        table.getColumns().clear();
        table.getColumns().addAll(colId, colDate, colCashier, colCust, colMethod, colTotal);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Getters
    public TableView<Transaction> getTable() { return table; }
    public Button getBtnRefresh() { return btnRefresh; }
    public Button getBtnExport() { return btnExport; } // Getter baru
}