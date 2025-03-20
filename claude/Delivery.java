import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class Delivery implements Runnable {
    private final Farm farm;
    private final TickManager tickManager;
    private final Random random = new Random();
    private final int DELIVERY_INTERVAL = 100; // Average ticks between deliveries
    private final int DELIVERY_SIZE = 10; // Number of animals per delivery

    public Delivery(Farm farm, TickManager tickManager) {
        this.farm = farm;
        this.tickManager = tickManager;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Calculate next delivery time with some randomness
                long nextDeliveryWait = (long) (DELIVERY_INTERVAL * (0.5 + random.nextDouble()));
                tickManager.waitForTicks(nextDeliveryWait);
                
                // Generate a random delivery of animals
                AnimalType[] delivery = new AnimalType[DELIVERY_SIZE];
                Map<AnimalType, Integer> deliveryCount = new HashMap<>();
                
                // Initialize counter for each animal type
                for (AnimalType type : AnimalType.values()) {
                    deliveryCount.put(type, 0);
                }
                
                // Randomly assign animals to the delivery
                for (int i = 0; i < DELIVERY_SIZE; i++) {
                    AnimalType type = AnimalType.values()[random.nextInt(AnimalType.values().length)];
                    delivery[i] = type;
                    deliveryCount.put(type, deliveryCount.get(type) + 1);
                }
                
                // Add the delivery to the enclosure
                farm.getEnclosure().addAnimals(delivery, tickManager.getTickCount());
                
                // Format and log the delivery
                StringBuilder logMessage = new StringBuilder();
                logMessage.append(tickManager.getTickCount()).append(" ")
                          .append(Thread.currentThread().getName())
                          .append(" Deposit_of_animals : ");
                
                for (Map.Entry<AnimalType, Integer> entry : deliveryCount.entrySet()) {
                    if (entry.getValue() > 0) {
                        logMessage.append(entry.getKey().toString().toLowerCase()).append("=")
                                  .append(entry.getValue()).append(" ");
                    }
                }
                
                System.out.println(logMessage.toString().trim());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
