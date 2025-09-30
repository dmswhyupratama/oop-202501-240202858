public class HelloProcedural {
    
    // Method 
    public static void sapa(String nama, String nim){
        System.out.println("Hello World I'm "+ nama + " -"+ nim);
    }

    public static void main(String[] args) {
        
        // Prosedural Tanpa Method
        String nama = "Dimas Wahyu Pratama";
        String nim = "240202858";
        System.out.println("Hello World I'm "+ nama + " -"+ nim);
        

        // Prosedural dengan method
        sapa("Dimas Wahyu Pratama", "240202858");
        // Bisa dipanggil secara berulang

    }

}