package com.upb.agripos.view;

import com.upb.agripos.model.Product;

import javafx.geometry.Insets;
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

public class ProductTableView {
    // Komponen Form
    private TextField txtCode = new TextField();
    private TextField txtName = new TextField();
    private TextField txtPrice = new TextField();
    private TextField txtStock = new TextField();
    private Button btnAdd = new Button("Tambah Produk");
    private Button btnDelete = new Button("Hapus Produk");

    // TableView
    private TableView<Product> table = new TableView<>();

    public Parent asParent() {
        VBox root = new VBox(15); // Jarak antar elemen vertikal diperlebar (10 -> 15)
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;"); // Background abu muda halus

        // 1. HEADER TITLE
        Label lblTitle = new Label("Agri-POS V2 (Management System)");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblTitle.setStyle("-fx-text-fill: #333333;");

        // 2. FORM INPUT (GridPane)
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15); 
        formGrid.setVgap(10);
        
        // Styling Label Form
        String labelStyle = "-fx-font-weight: bold; -fx-text-fill: #555555;";

        Label lblCode = new Label("Kode Produk:"); lblCode.setStyle(labelStyle);
        Label lblName = new Label("Nama Produk:"); lblName.setStyle(labelStyle);
        Label lblPrice = new Label("Harga (Rp):"); lblPrice.setStyle(labelStyle);
        Label lblStock = new Label("Stok (Unit):"); lblStock.setStyle(labelStyle);

        // Tambah Prompt Text (Placeholder)
        txtCode.setPromptText("Cth: P001");
        txtName.setPromptText("Cth: Pupuk Urea");
        txtPrice.setPromptText("Cth: 50000");
        txtStock.setPromptText("Cth: 100");

        formGrid.add(lblCode, 0, 0); formGrid.add(txtCode, 1, 0);
        formGrid.add(lblName, 0, 1); formGrid.add(txtName, 1, 1);
        formGrid.add(lblPrice, 0, 2); formGrid.add(txtPrice, 1, 2);
        formGrid.add(lblStock, 0, 3); formGrid.add(txtStock, 1, 3);

        // 3. TOMBOL (BUTTONS)
        // Styling Tombol Tambah (Hijau)
        btnAdd.setStyle(
            "-fx-background-color: #28a745; " + 
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );
        // Efek agar tombol terlihat sedikit lebar
        btnAdd.setPrefWidth(120);

        // Styling Tombol Hapus (Merah)
        btnDelete.setStyle(
            "-fx-background-color: #dc3545; " + 
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );
        btnDelete.setPrefWidth(120);

        // Grouping Tombol dalam HBox agar rapi bersebelahan
        HBox buttonBox = new HBox(10); // Jarak antar tombol 10px
        buttonBox.getChildren().addAll(btnAdd, btnDelete);
        
        // Menambahkan tombol ke Grid (posisi di bawah input stok)
        formGrid.add(buttonBox, 1, 4);

        // 4. SETUP TABLE
        TableColumn<Product, String> colCode = new TableColumn<>("Kode");
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        
        TableColumn<Product, String> colName = new TableColumn<>("Nama Produk");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Product, Double> colPrice = new TableColumn<>("Harga");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Product, Integer> colStock = new TableColumn<>("Stok");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().addAll(colCode, colName, colPrice, colStock);

        // Fitur: Kolom otomatis menyesuaikan lebar layar
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS); // Tabel ngisi sisa ruang ke bawah

        // MENYATUKAN SEMUA KE ROOT
        root.getChildren().addAll(lblTitle, new Separator(), formGrid, new Label("Daftar Produk:"), table);

        return root;
    }

    // Getter Components (Tetap sama)
    public TextField getTxtCode() { return txtCode; }
    public TextField getTxtName() { return txtName; }
    public TextField getTxtPrice() { return txtPrice; }
    public TextField getTxtStock() { return txtStock; }
    public Button getBtnAdd() { return btnAdd; }
    public Button getBtnDelete() { return btnDelete; }
    public TableView<Product> getTable() { return table; }
}