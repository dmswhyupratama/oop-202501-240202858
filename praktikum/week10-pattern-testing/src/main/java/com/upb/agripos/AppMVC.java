package com.upb.agripos;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.controller.ProductController;
import com.upb.agripos.model.Product;
import com.upb.agripos.view.ConsoleView;

public class AppMVC {
    public static void main(String[] args) {
        // Identitas Diri (Sesuai Modul)
        System.out.println("Hello, I am Dimas Wahyu Pratama-240202858 (Week10)");

        // 1. Tes Singleton
        System.out.println("\n--- Tes Singleton ---");
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        
        // Membuktikan objeknya sama
        if(db1 == db2) {
            System.out.println("Singleton Valid: Objek db1 dan db2 adalah instance yang sama.");
        }

        // 2. Tes MVC
        System.out.println("\n--- Tes MVC ---");
        Product product = new Product("P01", "Pupuk Organik");
        ConsoleView view = new ConsoleView();
        ProductController controller = new ProductController(product, view);
        
        controller.showProduct();
    }
}