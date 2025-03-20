public class FarmSimulator {
    public static void main(String[] args) {
        // Create farm and tick manager
        Farm farm = new Farm();
        TickManager tickManager = new TickManager();
        
        // Start the tick manager
        tickManager.startTicking();
        
        // Create and start farmers
        for (int i = 0; i < 3; i++) {
            Thread farmerThread = new Thread(new Farmer(i + 1, farm, tickManager));
            farmerThread.setName("Farmer-" + (i + 1));
            farmerThread.start();
        }
        
        // Create and start buyers
        for (int i = 0; i < 5; i++) {
            Thread buyerThread = new Thread(new Buyer("Buyer-" + (i + 1), farm.getFields(), tickManager));
            buyerThread.setName("Buyer-" + (i + 1));
            buyerThread.start();
        }
        
        // Create and start delivery thread
        Thread deliveryThread = new Thread(new Delivery(farm, tickManager));
        deliveryThread.setName("Delivery");
        deliveryThread.start();
        
        // Display current tick count
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("Current tick: " + tickManager.getTickCount());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
}
