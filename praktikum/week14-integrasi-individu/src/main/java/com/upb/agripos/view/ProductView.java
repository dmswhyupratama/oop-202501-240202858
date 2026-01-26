package com.upb.agripos.view;

import com.upb.agripos.model.Product;
import com.upb.agripos.util.AppStyles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProductView {

    // Komponen Form
    private TextField txtCode = new TextField();
    private TextField txtName = new TextField();
    private TextField txtPrice = new TextField();
    private TextField txtStock = new TextField();
    
    // Tombol Aksi
    private Button btnSave = new Button("Simpan Baru");
    private Button btnUpdate = new Button("Update Data");
    private Button btnDelete = new Button("Hapus");
    private Button btnClear = new Button("Clear Form");

    // Tabel
    private TableView<Product> table = new TableView<>();

    public Parent asParent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle(AppStyles.CARD_STYLE); // Pakai style card dari AppStyles

        // 1. JUDUL
        Label lblHeader = new Label("Manajemen Produk");
        lblHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblHeader.setTextFill(javafx.scene.paint.Color.web(AppStyles.COL_PRIMARY));

        // 2. FORM INPUT
        GridPane form = new GridPane();
        form.setHgap(15); form.setVgap(15);
        
        txtCode.setPromptText("Kode (Unik)");
        txtName.setPromptText("Nama Produk");
        txtPrice.setPromptText("Harga (Rp)");
        txtStock.setPromptText("Stok Awal");

        form.add(new Label("Kode Produk:"), 0, 0); form.add(txtCode, 1, 0);
        form.add(new Label("Nama Produk:"), 0, 1); form.add(txtName, 1, 1);
        form.add(new Label("Harga Satuan:"), 2, 0); form.add(txtPrice, 3, 0);
        form.add(new Label("Stok Barang:"), 2, 1); form.add(txtStock, 3, 1);

        // --- UPDATE BAGIAN TOMBOL AKSI ---
        HBox actionBox = new HBox(15); // Jarak antar tombol diperlebar sedikit (10 -> 15)
        actionBox.setAlignment(Pos.CENTER_LEFT); // Rata kiri agar sejajar dengan field input
        
        // Styling Tombol (Menggunakan style baru yang sudah ber-padding)
        btnSave.setStyle(AppStyles.BTN_SUCCESS);
        
        // PERBAIKAN: Pakai BTN_PRIMARY, bukan BTN_SIDEBAR_ACTIVE
        btnUpdate.setStyle(AppStyles.BTN_PRIMARY); 
        
        btnDelete.setStyle(AppStyles.BTN_DANGER);
        
        // Style manual untuk Clear (abu-abu), ditambahkan padding juga
        btnClear.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 15; -fx-cursor: hand;");
        
        // KITA HAPUS BAGIAN setPrefWidth AGAR LEBARNYA ALAMI
        // btnSave.setPrefWidth(100);
        // btnUpdate.setPrefWidth(100);
        // btnDelete.setPrefWidth(80);
        // btnClear.setPrefWidth(80);

        // Awalnya tombol Update & Delete disable
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        actionBox.getChildren().addAll(btnSave, btnUpdate, btnDelete, btnClear);
        form.add(actionBox, 1, 2, 3, 1); // Span 3 kolom

        // 4. TABEL PRODUK
        TableColumn<Product, String> colCode = new TableColumn<>("Kode");
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        
        TableColumn<Product, String> colName = new TableColumn<>("Nama Produk");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Product, Double> colPrice = new TableColumn<>("Harga");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Product, Integer> colStock = new TableColumn<>("Stok");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().addAll(colCode, colName, colPrice, colStock);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        root.getChildren().addAll(lblHeader, new Separator(), form, table);
        return root;
    }

    // Getter (Tidak Berubah)
    public TextField getTxtCode() { return txtCode; }
    public TextField getTxtName() { return txtName; }
    public TextField getTxtPrice() { return txtPrice; }
    public TextField getTxtStock() { return txtStock; }
    public Button getBtnSave() { return btnSave; }
    public Button getBtnUpdate() { return btnUpdate; }
    public Button getBtnDelete() { return btnDelete; }
    public Button getBtnClear() { return btnClear; }
    public TableView<Product> getTable() { return table; }
}