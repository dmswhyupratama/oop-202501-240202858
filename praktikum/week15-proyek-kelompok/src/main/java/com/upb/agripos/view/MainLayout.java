package com.upb.agripos.view;

import java.io.File;

import com.upb.agripos.controller.CashierController;
import com.upb.agripos.controller.ProductController;
import com.upb.agripos.controller.ReturnController;
import com.upb.agripos.model.User;
import com.upb.agripos.util.AppStyles;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainLayout {

    private Stage stage;
    private User currentUser;
    private BorderPane rootLayout;
    
    // Tombol Navigasi
    private Button btnKasir, btnRetur, btnProduk, btnLaporan, btnLogout;

    public MainLayout(Stage stage, User user) {
        this.stage = stage;
        this.currentUser = user;
    }

    public void show() {
        rootLayout = new BorderPane();
        
        // 1. SETUP SIDEBAR
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: " + AppStyles.COL_SIDEBAR + ";");
        sidebar.setPadding(new Insets(20, 0, 20, 0));

        Label lblBrand = new Label("AGRI-POS");
        lblBrand.setTextFill(javafx.scene.paint.Color.WHITE);
        lblBrand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        lblBrand.setPadding(new Insets(0, 0, 20, 20));

        Label lblUser = new Label("Halo, " + currentUser.getUsername());
        lblUser.setTextFill(javafx.scene.paint.Color.LIGHTGRAY);
        lblUser.setPadding(new Insets(0, 0, 10, 20));

        btnKasir = createMenuButton("Transaksi Penjualan");
        btnRetur = createMenuButton("Retur Barang");
        btnProduk = createMenuButton("Kelola Produk");
        btnLaporan = createMenuButton("Laporan Penjualan");
        
        btnLogout = new Button("Logout");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle(AppStyles.BTN_SIDEBAR_LOGOUT);
        
        btnKasir.setOnAction(e -> showKasir());
        btnRetur.setOnAction(e -> showRetur());
        btnProduk.setOnAction(e -> showProduk());
        btnLaporan.setOnAction(e -> showLaporan());
        btnLogout.setOnAction(e -> new LoginView(stage).show());

        if (currentUser.getRole().equals("KASIR")) {
            sidebar.getChildren().addAll(lblBrand, lblUser, new Separator(), btnKasir, btnRetur);
            showKasir(); 
        } else {
            sidebar.getChildren().addAll(lblBrand, lblUser, new Separator(), btnProduk, btnLaporan);
            showProduk();
        }
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().addAll(spacer, btnLogout);

        rootLayout.setLeft(sidebar);

        Scene scene = new Scene(rootLayout, 1000, 700); 
        stage.setTitle("Agri-POS System - " + currentUser.getFullname());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE); 
        btn.setStyle(AppStyles.BTN_SIDEBAR);
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().contains(AppStyles.BTN_SIDEBAR_ACTIVE)) 
                btn.setStyle("-fx-background-color: rgba(255,255,255,0.1); " + AppStyles.BTN_SIDEBAR.replace("-fx-background-color: transparent;", ""));
        });
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().contains(AppStyles.BTN_SIDEBAR_ACTIVE))
                btn.setStyle(AppStyles.BTN_SIDEBAR);
        });
        return btn;
    }

    private void setActiveButton(Button activeBtn) {
        if(btnKasir != null) btnKasir.setStyle(AppStyles.BTN_SIDEBAR);
        if(btnRetur != null) btnRetur.setStyle(AppStyles.BTN_SIDEBAR);
        if(btnProduk != null) btnProduk.setStyle(AppStyles.BTN_SIDEBAR);
        if(btnLaporan != null) btnLaporan.setStyle(AppStyles.BTN_SIDEBAR);
        if(activeBtn != null) activeBtn.setStyle(AppStyles.BTN_SIDEBAR_ACTIVE);
    }

    // --- METODE GANTI HALAMAN ---

    private void showKasir() {
        setActiveButton(btnKasir);
        com.upb.agripos.dao.ProductDAOImpl productDAO = new com.upb.agripos.dao.ProductDAOImpl();
        com.upb.agripos.service.ProductService productService = new com.upb.agripos.service.ProductService(productDAO);
        com.upb.agripos.dao.TransactionDAOImpl transactionDAO = new com.upb.agripos.dao.TransactionDAOImpl();
        CashierView view = new CashierView();
        new CashierController(view, productService, transactionDAO, currentUser);
        rootLayout.setCenter(createContentWrapper(view.asParent(), "Transaksi Penjualan"));
    }

    private void showRetur() {
        setActiveButton(btnRetur);
        ReturnView view = new ReturnView();
        new ReturnController(view);
        rootLayout.setCenter(createContentWrapper(view.asParent(), "Retur Barang"));
    }

    private void showProduk() {
        setActiveButton(btnProduk);
        com.upb.agripos.dao.ProductDAOImpl dao = new com.upb.agripos.dao.ProductDAOImpl();
        com.upb.agripos.service.ProductService service = new com.upb.agripos.service.ProductService(dao);
        ProductView view = new ProductView();
        new ProductController(view, service);
        rootLayout.setCenter(createContentWrapper(view.asParent(), "Manajemen Produk"));
    }

    // --- [FIXED] SHOW LAPORAN ---
    private void showLaporan() {
        setActiveButton(btnLaporan);
        
        com.upb.agripos.dao.TransactionDAOImpl dao = new com.upb.agripos.dao.TransactionDAOImpl();
        com.upb.agripos.service.ReportService service = new com.upb.agripos.service.ReportService(dao);
        
        ReportView view = new ReportView();
        
        // 1. Tampilkan View (Panggil asParent agar kolom terbentuk!)
        rootLayout.setCenter(view.asParent()); 
        
        // 2. Logic Load Data
        Runnable loadData = () -> {
            view.getTable().getItems().setAll(service.getSalesReport());
        };
        
        view.getBtnRefresh().setOnAction(e -> loadData.run());
        
        // 3. Logic Export CSV
        view.getBtnExport().setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Simpan Laporan Penjualan");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
            fileChooser.setInitialFileName("Laporan_AgriPOS_" + timestamp + ".csv");
            
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                boolean success = service.exportToCSV(file.getAbsolutePath());
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export Berhasil");
                    alert.setHeaderText(null);
                    alert.setContentText("Laporan tersimpan di:\n" + file.getAbsolutePath());
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gagal");
                    alert.setContentText("Gagal menyimpan file.");
                    alert.show();
                }
            }
        });

        // Load data awal
        loadData.run();
    }

    private VBox createContentWrapper(javafx.scene.Node content, String title) {
        VBox wrapper = new VBox(20);
        wrapper.setPadding(new Insets(30));
        wrapper.setStyle("-fx-background-color: #F4F6F8;"); 
        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        lblTitle.setTextFill(javafx.scene.paint.Color.web("#333"));
        wrapper.getChildren().addAll(lblTitle, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        return wrapper;
    }
}