package com.upb.agripos.view;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.util.AppStyles;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
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

public class CashierView {

    // --- BAGIAN KIRI: DAFTAR PRODUK ---
    private TextField txtSearch = new TextField();
    private Button btnRefresh = new Button("Refresh");
    private TableView<Product> tblProduct = new TableView<>();
    
    // Input Penambahan
    private TextField txtQtyInput = new TextField();
    private Button btnAddToFlow = new Button("Masukan ke Keranjang ->");

    // --- BAGIAN KANAN: KERANJANG ---
    private TableView<CartItem> tblCart = new TableView<>();
    private Button btnEditQty = new Button("Ubah Qty");
    private Button btnRemoveItem = new Button("Hapus");
    private Button btnClearCart = new Button("Kosongkan");

    // Pembayaran & Kalkulasi
    private ComboBox<String> cmbPaymentMethod = new ComboBox<>();
    private Label lblSubtotal = new Label("Rp 0");
    private Label lblTax = new Label("Rp 0");
    private Label lblGrandTotal = new Label("Rp 0");
    
    // --- FITUR BARU: VOUCHER & DISKON ---
    private TextField txtVoucher = new TextField();
    private Button btnCheckVoucher = new Button("Cek");
    private Label lblDiscountInfo = new Label(""); // Info voucher aktif
    private Label lblDiscount = new Label("-Rp 0"); // Total Nominal Diskon
    
    // Input Bayar & Kembalian
    private TextField txtPayment = new TextField(); // Input Uang Pelanggan
    private Label lblChange = new Label("Rp 0");    // Kembalian
    
    private Button btnCheckout = new Button("BAYAR / CHECKOUT");

    public Parent asParent() {
        SplitPane splitPane = new SplitPane();
        splitPane.setStyle("-fx-background-color: transparent;");

        // === PANEL KIRI (KATALOG) ===
        VBox leftPane = new VBox(10);
        leftPane.setPadding(new Insets(15));
        leftPane.setStyle("-fx-background-color: white;");

        Label lblKatalog = new Label("Katalog Produk");
        lblKatalog.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblKatalog.setTextFill(javafx.scene.paint.Color.web(AppStyles.COL_PRIMARY));

        HBox searchBox = new HBox(10);
        txtSearch.setPromptText("Cari Nama / Kode Barang...");
        txtSearch.setPrefWidth(250);
        btnRefresh.setStyle("-fx-cursor: hand;");
        searchBox.getChildren().addAll(txtSearch, btnRefresh);

        setupProductTable();
        VBox.setVgrow(tblProduct, Priority.ALWAYS);

        HBox actionPanel = new HBox(10);
        actionPanel.setAlignment(Pos.CENTER_LEFT);
        actionPanel.setPadding(new Insets(10));
        actionPanel.setStyle("-fx-background-color: #f0f2f5; -fx-background-radius: 5;");
        
        txtQtyInput.setPromptText("Jml");
        txtQtyInput.setPrefWidth(60);
        btnAddToFlow.setStyle(AppStyles.BTN_PRIMARY);
        
        actionPanel.getChildren().addAll(new Label("Jumlah Beli:"), txtQtyInput, btnAddToFlow);

        leftPane.getChildren().addAll(lblKatalog, searchBox, tblProduct, actionPanel);

        // === PANEL KANAN (KERANJANG) ===
        VBox rightPane = new VBox(10);
        rightPane.setPadding(new Insets(15));
        rightPane.setStyle("-fx-background-color: #f8f9fa;");

        Label lblCart = new Label("Keranjang Belanja");
        lblCart.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        
        setupCartTable();
        VBox.setVgrow(tblCart, Priority.ALWAYS);
        tblCart.setPlaceholder(new Label("Keranjang masih kosong"));

        HBox cartTools = new HBox(10);
        btnEditQty.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
        btnRemoveItem.setStyle(AppStyles.BTN_DANGER);
        btnClearCart.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white;");
        cartTools.getChildren().addAll(btnEditQty, btnRemoveItem, btnClearCart);

        // Footer Pembayaran (GridPane)
        GridPane footer = new GridPane();
        footer.setHgap(15); footer.setVgap(8);
        footer.setAlignment(Pos.CENTER_RIGHT);
        
        cmbPaymentMethod.getItems().addAll("CASH", "E-WALLET");
        cmbPaymentMethod.setValue("CASH");

        // STYLING KOMPONEN BARU
        lblDiscount.setTextFill(javafx.scene.paint.Color.RED);
        lblDiscountInfo.setStyle("-fx-font-size: 10px; -fx-text-fill: blue;");
        
        txtVoucher.setPromptText("Kode Voucher");
        txtVoucher.setPrefWidth(120);
        btnCheckVoucher.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");

        lblGrandTotal.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblGrandTotal.setTextFill(javafx.scene.paint.Color.web(AppStyles.COL_ACCENT));
        
        // Style Input Bayar
        txtPayment.setPromptText("Input Nominal");
        txtPayment.setPrefWidth(120);
        
        // Style Label Kembalian
        lblChange.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        lblChange.setTextFill(javafx.scene.paint.Color.web("#27ae60")); // Hijau

        btnCheckout.setStyle(AppStyles.BTN_SUCCESS);
        btnCheckout.setPrefWidth(Double.MAX_VALUE);
        btnCheckout.setPrefHeight(40);

        // --- UPDATE LAYOUT FOOTER ---
        
        // Baris 0: Voucher (BARU)
        HBox voucherBox = new HBox(5);
        voucherBox.setAlignment(Pos.CENTER_RIGHT);
        voucherBox.getChildren().addAll(txtVoucher, btnCheckVoucher);
        footer.add(new Label("Kode Member:"), 0, 0);  footer.add(voucherBox, 1, 0);
        footer.add(lblDiscountInfo, 1, 1); // Info status di bawah input

        // Baris 2: Subtotal
        footer.add(new Label("Subtotal:"), 0, 2);     footer.add(lblSubtotal, 1, 2);
        
        // Baris 3: Diskon Total (BARU)
        footer.add(new Label("Total Diskon:"), 0, 3); footer.add(lblDiscount, 1, 3);
        
        // Baris 4: Pajak
        footer.add(new Label("Pajak/Adm:"), 0, 4);    footer.add(lblTax, 1, 4);
        
        // Baris 5: Separator
        footer.add(new Separator(), 0, 5, 2, 1);
        
        // Baris 6: Total Tagihan
        footer.add(new Label("TOTAL TAGIHAN:"), 0, 6);footer.add(lblGrandTotal, 1, 6);
        
        // Baris 7: Input Bayar
        footer.add(new Label("Bayar (Rp):"), 0, 7);   footer.add(txtPayment, 1, 7);
        
        // Baris 8: Kembalian
        footer.add(new Label("Kembalian:"), 0, 8);    footer.add(lblChange, 1, 8);

        rightPane.getChildren().addAll(lblCart, tblCart, cartTools, new Separator(), footer, btnCheckout);

        splitPane.getItems().addAll(leftPane, rightPane);
        splitPane.setDividerPositions(0.6);

        return splitPane;
    }

    private void setupProductTable() {
        TableColumn<Product, String> colCode = new TableColumn<>("Kode");
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        
        TableColumn<Product, String> colName = new TableColumn<>("Nama Produk");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Product, Double> colPrice = new TableColumn<>("Harga");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<Product, Integer> colStock = new TableColumn<>("Stok");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tblProduct.getColumns().addAll(colCode, colName, colPrice, colStock);
        tblProduct.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupCartTable() {
        TableColumn<CartItem, String> colName = new TableColumn<>("Item");
        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        
        TableColumn<CartItem, Integer> colQty = new TableColumn<>("Qty");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colQty.setPrefWidth(50);
        
        TableColumn<CartItem, Double> colTotal = new TableColumn<>("Subtotal");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tblCart.getColumns().addAll(colName, colQty, colTotal);
        tblCart.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Getters
    public TextField getTxtSearch() { return txtSearch; }
    public Button getBtnRefresh() { return btnRefresh; }
    public TableView<Product> getTblProduct() { return tblProduct; }
    public TextField getTxtQtyInput() { return txtQtyInput; }
    public Button getBtnAddToFlow() { return btnAddToFlow; }
    public TableView<CartItem> getTblCart() { return tblCart; }
    public Button getBtnEditQty() { return btnEditQty; }
    public Button getBtnRemoveItem() { return btnRemoveItem; }
    public Button getBtnClearCart() { return btnClearCart; }
    
    public ComboBox<String> getCmbPaymentMethod() { return cmbPaymentMethod; }
    public Label getLblSubtotal() { return lblSubtotal; }
    public Label getLblTax() { return lblTax; }
    public Label getLblGrandTotal() { return lblGrandTotal; }
    
    // GETTER BARU (VOUCHER)
    public TextField getTxtVoucher() { return txtVoucher; }
    public Button getBtnCheckVoucher() { return btnCheckVoucher; }
    public Label getLblDiscountInfo() { return lblDiscountInfo; }
    public Label getLblDiscount() { return lblDiscount; }
    
    // Getter Bayar
    public TextField getTxtPayment() { return txtPayment; }
    public Label getLblChange() { return lblChange; }
    
    public Button getBtnCheckout() { return btnCheckout; }
}