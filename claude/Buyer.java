import java.util.Map;
import java.util.Random;

public class Buyer implements Runnable {
    private final String name;
    private final Map<AnimalType, Field> fields;
    private final TickManager tickManager;
    private final Random random = new Random();
    private final int BUYING_INTERVAL = 10; // Average ticks between buying

    public Buyer(String name, Map<AnimalType, Field> fields, TickManager tickManager) {
        this.name = name;
        this.fields = fields;
        this.tickManager = tickManager;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Calculate next buying time with some randomness
                long nextBuyingWait = (long) (BUYING_INTERVAL * (0.5 + random.nextDouble()));
                tickManager.waitForTicks(nextBuyingWait);
                
                // Randomly choose animal type to buy
                AnimalType chosenType = AnimalType.values()[random.nextInt(AnimalType.values().length)];
                Field field = fields.get(chosenType);
                
                long startWaitTick = tickManager.getTickCount();
                long waitedTicks = 0;
                
                // Try to buy an animal
                boolean animalBought = false;
                while (!animalBought) {
                    field.getLock().lock();
                    try {
                        // Check if field is being stocked
                        if (field.isBeingStocked()) {
                            field.getLock().unlock();
                            tickManager.waitForTicks(1);
                            continue;
                        }
                        
                        // Try to remove an animal
                        if (field.getAnimalCount() > 0) {
                            field.removeAnimal();
                            animalBought = true;
                            waitedTicks = tickManager.getTickCount() - startWaitTick;
                            
                            // It takes 1 tick to take an animal
                            tickManager.waitForTicks(1);
                            
                            System.out.println(tickManager.getTickCount() + " " + name + 
                                    " buyer=" + name.substring(6) + " collected_from_field=" + 
                                    chosenType.toString().toLowerCase() + " waited_ticks=" + waitedTicks);
                        } else {
                            // No animal available, wait and try again
                            field.getLock().unlock();
                            tickManager.waitForTicks(1);
                            continue;
                        }
                    } finally {
                        if (field.getLock().isHeldByCurrentThread()) {
                            field.getLock().unlock();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getName() {
        return name;
    }
}
