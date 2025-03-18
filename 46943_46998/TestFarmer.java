public class TestFarmer {
    public static void main(String[] args) {
        Farm farm = new Farm();
        TickManager tickManager = new TickManager();
        tickManager.startTicking();

        Farmer farmer1 = new Farmer(1, farm, tickManager);
        farmer1.start();
    }
}
