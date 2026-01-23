package com.upb.agripos;

import com.upb.agripos.controller.ProductController;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl; 
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.ProductTableView; 

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Inisialisasi Backend (DAO & Service)
        ProductDAO dao = new ProductDAOImpl(); 
        ProductService service = new ProductService(dao);

        // 2. Inisialisasi View (Gunakan ProductTableView untuk Week 13)
        ProductTableView view = new ProductTableView();

        // 3. Inisialisasi Controller (Binding View & Service)
        new ProductController(view, service);

        // 4. Tampilkan Scene
        Scene scene = new Scene(view.asParent(), 550, 600);
        
        stage.setTitle("Agri-POS (Week 13) - Kelola Produk");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}