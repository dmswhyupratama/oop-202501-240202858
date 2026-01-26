package com.upb.agripos.view;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.User;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ReceiptDialog {

    // UPDATE: Parameter bertambah menjadi 8 (tambah discountAmount)
    public static void show(Transaction trx, List<CartItem> items, User cashier, double subtotal, double discountAmount, double tax, double payAmount, double changeAmount) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cetak Struk - Agri POS");
        alert.setHeaderText("Transaksi Berhasil!");

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateStr = sdf.format(new Date());

        sb.append("================================\n");
        sb.append("           AGRI-POS             \n");
        sb.append("     Solusi Pertanian Maju      \n");
        sb.append("================================\n");
        sb.append("No Trx  : ").append(trx.getId()).append("\n");
        sb.append("Tanggal : ").append(dateStr).append("\n");
        sb.append("Kasir   : ").append(cashier.getUsername()).append("\n");
        sb.append("--------------------------------\n");
        
        for (CartItem item : items) {
            sb.append(item.getProduct().getName()).append("\n");
            sb.append(String.format("   %d x %s = %s\n", 
                item.getQuantity(), 
                formatRupiah(item.getProduct().getPrice()), 
                formatRupiah(item.getSubtotal())
            ));
        }

        sb.append("--------------------------------\n");
        sb.append(String.format("Subtotal      : %s\n", formatRupiah(subtotal)));
        
        // TAMPILKAN DISKON JIKA ADA
        if (discountAmount > 0) {
            sb.append(String.format("Total Diskon  : -%s\n", formatRupiah(discountAmount)));
        }
        
        if (tax > 0) {
            sb.append(String.format("Biaya Layanan : %s\n", formatRupiah(tax)));
        }
        
        sb.append("--------------------------------\n");
        sb.append(String.format("TOTAL TAGIHAN : %s\n", formatRupiah(trx.getTotalAmount())));
        sb.append("--------------------------------\n");
        
        sb.append(String.format("Metode Bayar  : %s\n", trx.getPaymentMethod()));
        sb.append(String.format("Tunai/Bayar   : %s\n", formatRupiah(payAmount)));
        sb.append(String.format("Kembali       : %s\n", formatRupiah(changeAmount)));
        
        sb.append("================================\n");
        sb.append("     Terima Kasih Petani!       \n");
        sb.append("    Barang yg dibeli tidak      \n");
        sb.append("      dapat dikembalikan        \n");
        sb.append("================================\n");

        TextArea textArea = new TextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setStyle("-fx-font-family: 'Consolas', 'Monospaced'; -fx-font-size: 12px;");

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setContent(expContent);
        alert.showAndWait();
    }

    private static String formatRupiah(double number) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return nf.format(number).replace("Rp", "Rp ");
    }
}