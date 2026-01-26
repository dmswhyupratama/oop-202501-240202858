package com.upb.agripos.view;

import com.upb.agripos.model.Transaction;
import com.upb.agripos.util.AppStyles;

import javafx.geometry.Insets;
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

    public Parent asParent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle(AppStyles.CARD_STYLE);

        // 1. HEADER
        HBox header = new HBox(10);
        Label lblTitle = new Label("Laporan Penjualan");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblTitle.setTextFill(javafx.scene.paint.Color.web(AppStyles.COL_PRIMARY));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        btnRefresh.setStyle(AppStyles.BTN_SUCCESS);
        
        header.getChildren().addAll(lblTitle, spacer, btnRefresh);

        // 2. TABEL TRANSAKSI
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
        colTotal.setCellValueFactory(new PropertyValueFactory<>("formattedTotal")); // Pakai getter helper
        colTotal.setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");

        table.getColumns().addAll(colId, colDate, colCashier, colCust, colMethod, colTotal);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        root.getChildren().addAll(header, new Separator(), table);
        return root;
    }

    public TableView<Transaction> getTable() { return table; }
    public Button getBtnRefresh() { return btnRefresh; }
}