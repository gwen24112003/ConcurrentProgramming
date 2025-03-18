public class TestFarmer {
    public static void main(String[] args) {
        Farm farm = new Farm();

        System.out.println("Starting farmer simulation...");
        Farmer farmer1 = new Farmer(1, farm);
        farmer1.start();
    }
}
