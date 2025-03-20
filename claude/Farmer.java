import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Farmer implements Runnable {
    private final int id;
    private final Farm farm;
    private final TickManager tickManager;
    private final String name;
    private static final int MAX_ANIMALS_CARRY = 10;

    public Farmer(int id, Farm farm, TickManager tickManager) {
        this.id = id;
        this.farm = farm;
        this.tickManager = tickManager;
        this.name = "Farmer-" + id;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Collect animals from enclosure
                Map<AnimalType, Integer> collectedAnimals = collectAnimalsFromEnclosure();
                
                if (collectedAnimals.isEmpty()) {
                    // No animals to collect, wait for next tick
                    tickManager.waitForTicks(1);
                    continue;
                }
                
                // Calculate total number of animals collected
                int totalCollected = 0;
                for (int count : collectedAnimals.values()) {
                    totalCollected += count;
                }
                
                // Format and log the collected animals
                StringBuilder collectLogMessage = new StringBuilder();
                collectLogMessage.append(tickManager.getTickCount()).append(" ")
                                .append(name)
                                .append(" farmer=").append(id)
                                .append(" collected_animals waited_ticks=0: ");
                
                for (Map.Entry<AnimalType, Integer> entry : collectedAnimals.entrySet()) {
                    if (entry.getValue() > 0) {
                        collectLogMessage.append(entry.getKey().toString().toLowerCase()).append("=")
                                        .append(entry.getValue()).append(" ");
                    }
                }
                
                System.out.println(collectLogMessage.toString().trim());
                
                // Move animals to fields
                moveAnimalsToFields(collectedAnimals);
                
                // Return to enclosure (10 ticks)
                tickManager.waitForTicks(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Map<AnimalType, Integer> collectAnimalsFromEnclosure() throws InterruptedException {
        Map<AnimalType, Integer> collectedAnimals = new HashMap<>();
        int totalCollected = 0;
        
        // Initialize counter for each animal type
        for (AnimalType type : AnimalType.values()) {
            collectedAnimals.put(type, 0);
        }
        
        // Collect up to MAX_ANIMALS_CARRY animals
        while (totalCollected < MAX_ANIMALS_CARRY) {
            Enclosure enclosure = farm.getEnclosure();
            AnimalType animal = enclosure.takeAnimal();
            
            if (animal == null) {
                // No more animals in enclosure
                break;
            }
            
            collectedAnimals.put(animal, collectedAnimals.get(animal) + 1);
            totalCollected++;
        }
        
        return collectedAnimals;
    }

    private void moveAnimalsToFields(Map<AnimalType, Integer> collectedAnimals) throws InterruptedException {
        // Keep track of our current position (enclosure = null, or field type)
        AnimalType currentPosition = null;
        Map<AnimalType, Integer> remainingAnimals = new HashMap<>(collectedAnimals);
        
        // Process each animal type
        for (AnimalType type : AnimalType.values()) {
            int count = collectedAnimals.getOrDefault(type, 0);
            if (count <= 0) continue;
            
            // Calculate travel time
            int travelTime;
            if (currentPosition == null) {
                // Coming from enclosure
                travelTime = 10 + getTotalRemainingAnimals(remainingAnimals);
            } else {
                // Coming from another field
                travelTime = 10 + getTotalRemainingAnimals(remainingAnimals);
            }
            
            // Wait for travel time
            tickManager.waitForTicks(travelTime);
            
            // Update current position
            currentPosition = type;
            
            // Begin stocking field
            Field field = farm.getField(type);
            field.getLock().lock();
            
            try {
                // Mark field as being stocked
                field.setBeingStocked(true);
                
                // Log beginning of stocking
                System.out.println(tickManager.getTickCount() + " " + name + 
                        " farmer=" + id + " began_stocking_field : " + 
                        type.toString().toLowerCase() + "=" + count);
                
                // Stock animals
                int stockedCount = 0;
                for (int i = 0; i < count; i++) {
                    if (field.addAnimal()) {
                        stockedCount++;
                        // It takes 1 tick to put an animal in a field
                        tickManager.waitForTicks(1);
                    } else {
                        // Field is full
                        break;
                    }
                }
                
                // Log end of stocking
                System.out.println(tickManager.getTickCount() + " " + name + 
                        " farmer=" + id + " finished_stocking_field : " + 
                        type.toString().toLowerCase() + "=" + stockedCount);
                
                // Update remaining animals
                remainingAnimals.put(type, remainingAnimals.get(type) - stockedCount);
            } finally {
                // Mark field as no longer being stocked
                field.setBeingStocked(false);
                field.getLock().unlock();
            }
        }
    }
    
    private int getTotalRemainingAnimals(Map<AnimalType, Integer> remainingAnimals) {
        int total = 0;
        for (int count : remainingAnimals.values()) {
            total += count;
        }
        return total;
    }
}
