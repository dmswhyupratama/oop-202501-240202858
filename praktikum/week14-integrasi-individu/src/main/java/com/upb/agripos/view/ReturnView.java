package com.upb.agripos.view;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.util.AppStyles;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ReturnView {

    // Komponen Pencarian
    private TextField txtTrxId = new TextField();
    private Button btnSearch = new Button("Cari Transaksi");

    // Tabel Detail Transaksi
    private TableView<CartItem> table = new TableView<>();

    // Form Retur
    private TextField txtReturnQty = new TextField();
    private TextArea txtReason = new TextArea();
    private Button btnProcessReturn = new Button("Proses Retur Barang");

    public Parent asParent() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle(AppStyles.CARD_STYLE);

        // 1. Header
        Label lblTitle = new Label("Retur Barang");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblTitle.setTextFill(javafx.scene.paint.Color.web(AppStyles.COL_PRIMARY));

        // 2. Search Box
        HBox searchBox = new HBox(10);
        txtTrxId.setPromptText("Masukkan ID Transaksi (Contoh: TRX-2026...)");
        txtTrxId.setPrefWidth(300);
        btnSearch.setStyle(AppStyles.BTN_PRIMARY);
        searchBox.getChildren().addAll(new Label("No. Struk:"), txtTrxId, btnSearch);

        // 3. Tabel Barang
        TableColumn<CartItem, String> colCode = new TableColumn<>("Kode");
        colCode.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getProduct().getCode()));
        
        TableColumn<CartItem, String> colName = new TableColumn<>("Nama Produk");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        
        TableColumn<CartItem, Integer> colQty = new TableColumn<>("Qty Dibeli");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table.getColumns().addAll(colCode, colName, colQty);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(200);

        // 4. Form Eksekusi
        VBox formBox = new VBox(10);
        formBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 5;");
        
        Label lblForm = new Label("Form Pengembalian");
        lblForm.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        txtReturnQty.setPromptText("Jumlah yang dikembalikan");
        txtReturnQty.setMaxWidth(200);
        
        txtReason.setPromptText("Alasan pengembalian (Misal: Rusak/Kadaluarsa)");
        txtReason.setPrefRowCount(3);
        txtReason.setMaxWidth(400);

        btnProcessReturn.setStyle(AppStyles.BTN_DANGER); // Merah karena retur itu 'danger' zone
        btnProcessReturn.setDisable(true); // Disable dulu sebelum pilih barang

        formBox.getChildren().addAll(lblForm, new Label("Jumlah Retur:"), txtReturnQty, new Label("Alasan:"), txtReason, btnProcessReturn);

        root.getChildren().addAll(lblTitle, new Separator(), searchBox, table, new Separator(), formBox);
        return root;
    }

    // Getter
    public TextField getTxtTrxId() { return txtTrxId; }
    public Button getBtnSearch() { return btnSearch; }
    public TableView<CartItem> getTable() { return table; }
    public TextField getTxtReturnQty() { return txtReturnQty; }
    public TextArea getTxtReason() { return txtReason; }
    public Button getBtnProcessReturn() { return btnProcessReturn; }
}