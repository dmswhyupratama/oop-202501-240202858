import java.util.function.BiConsumer;

public class HelloFunctional {
   
    public static void main(String[] args) {
        // Membuat fungsi "sapa" menggunakan lambda
        BiConsumer<String, String> sapa = 
            (nama, nim) -> System.out.println("Hello World, I am " + nama + " -" + nim);

        // eksekusi fungsi
        sapa.accept("Dimas Wahyu Pratama", "240202858");
        System.out.println("Program Functional Selesai, Terimakasih");
    }

}