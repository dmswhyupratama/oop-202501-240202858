package com.upb.agripos;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductFormView;
import com.upb.agripos.controller.ProductController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Inisialisasi Backend (DAO & Service)
        // Pastikan ProductDAO kamu punya implementasi koneksi DB yang benar dari Week 11
        ProductDAO dao = new com.upb.agripos.dao.ProductDAOImpl(); 
        ProductService service = new ProductService(dao);

        // 2. Inisialisasi View
        ProductFormView view = new ProductFormView();

        // 3. Inisialisasi Controller (Binding View & Service)
        new ProductController(view, service);

        // 4. Tampilkan Scene
        Scene scene = new Scene(view.asParent(), 400, 500);
        stage.setTitle("Agri-POS - Kelola Produk");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}