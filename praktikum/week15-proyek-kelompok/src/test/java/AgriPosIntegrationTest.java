
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.dao.ReturnDAO;
import com.upb.agripos.dao.TransactionDAOImpl;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.model.Transaction;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AgriPosIntegrationTest {

    static ProductDAOImpl productDAO;
    static TransactionDAOImpl transactionDAO;
    static ReturnDAO returnDAO;

    static final String TEST_PROD_CODE = "TEST-999";
    static final String TEST_TRX_ID = "TRX-TEST-001";
    static final int TEST_INITIAL_STOCK = 100;
    static final int BUY_QTY = 10;
    static final int RETURN_QTY = 5;

    @BeforeAll
    public static void setup() throws Exception {
        System.out.println("=== 1. SETUP ===");
        try {
            productDAO = new ProductDAOImpl();
            transactionDAO = new TransactionDAOImpl();
            returnDAO = new ReturnDAO();

            cleanUpData();

            String sql = "INSERT INTO products (code, name, price, stock) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, TEST_PROD_CODE);
                ps.setString(2, "Produk Testing JUnit");
                ps.setDouble(3, 10000);
                ps.setInt(4, TEST_INITIAL_STOCK);
                ps.executeUpdate();
            }
            System.out.println("--> Produk Dummy Berhasil Dibuat.");
        } catch (Throwable t) {
            System.err.println("GAGAL SAAT SETUP:");
            t.printStackTrace(); // CETAK ERROR KE LAYAR
            throw t;
        }
    }

    @Test
    @Order(1)
    public void test1_DatabaseConnection() {
        System.out.println("Test 1: Cek Koneksi");
        try {
            Connection conn = DatabaseConnection.getConnection();
            Assertions.assertNotNull(conn);
            Assertions.assertFalse(conn.isClosed());
            System.out.println("--> Koneksi Aman.");
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    @Test
    @Order(2)
    public void test2_TransactionProcess() throws Exception {
        System.out.println("Test 2: Simulasi Transaksi");
        try {
            Product p = productDAO.findByCode(TEST_PROD_CODE);
            Assertions.assertNotNull(p, "Produk testing tidak ditemukan di awal!");

            List<CartItem> items = new ArrayList<>();
            items.add(new CartItem(p, BUY_QTY)); 

            double total = p.getPrice() * BUY_QTY;
            Transaction trx = new Transaction(TEST_TRX_ID, total, "CASH", 1, 0, 0);

            transactionDAO.save(trx, items);

            Product pAfter = productDAO.findByCode(TEST_PROD_CODE);
            int expectedStock = TEST_INITIAL_STOCK - BUY_QTY; 
            
            System.out.println("--> Stok Awal: " + TEST_INITIAL_STOCK + ", Stok Akhir: " + pAfter.getStock());
            Assertions.assertEquals(expectedStock, pAfter.getStock(), "Stok tidak berkurang!");
            
        } catch (Throwable t) {
            System.err.println("GAGAL DI TEST 2:");
            t.printStackTrace(); // CETAK ERROR
            throw t;
        }
    }

    @Test
    @Order(3)
    public void test3_ReturnProcess() throws Exception {
        System.out.println("Test 3: Simulasi Retur");
        try {
            Product pBefore = productDAO.findByCode(TEST_PROD_CODE);
            int stockBeforeReturn = pBefore.getStock();

            returnDAO.processReturn(TEST_TRX_ID, TEST_PROD_CODE, RETURN_QTY, "Cacat (Test)");

            Product pAfter = productDAO.findByCode(TEST_PROD_CODE);
            int expectedStock = stockBeforeReturn + RETURN_QTY;
            
            System.out.println("--> Stok Sebelum Retur: " + stockBeforeReturn + ", Stok Akhir: " + pAfter.getStock());
            Assertions.assertEquals(expectedStock, pAfter.getStock(), "Stok tidak bertambah!");
            
        } catch (Throwable t) {
            System.err.println("GAGAL DI TEST 3:");
            t.printStackTrace();
            throw t;
        }
    }

    @Test
    @Order(4)
    public void test4_ValidateHistory() {
        System.out.println("Test 4: Cek History");
        try {
            List<CartItem> items = transactionDAO.getTransactionItems(TEST_TRX_ID);
            Assertions.assertFalse(items.isEmpty(), "History kosong!");
            System.out.println("--> History ditemukan.");
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("=== CLEANUP ===");
        cleanUpData();
    }

    private static void cleanUpData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.createStatement().executeUpdate("DELETE FROM returns WHERE transaction_id = '" + TEST_TRX_ID + "'");
            conn.createStatement().executeUpdate("DELETE FROM transaction_details WHERE transaction_id = '" + TEST_TRX_ID + "'");
            conn.createStatement().executeUpdate("DELETE FROM transactions WHERE id = '" + TEST_TRX_ID + "'");
            conn.createStatement().executeUpdate("DELETE FROM products WHERE code = '" + TEST_PROD_CODE + "'");
        } catch (Exception e) {
            System.err.println("Gagal Cleanup: " + e.getMessage());
        }
    }
}