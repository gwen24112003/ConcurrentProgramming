import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Represents a farmer who moves animals from the enclosure to fields.
 */
public class Farmer extends Thread {
    private final int id;
    private final Farm farm;
    private static final int max_animals_carry = 10;
    private final Random random = new Random();

    public Farmer(int id, Farm farm) {
        this.id = id;
        this.farm = farm;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<AnimalType> carriedAnimals = collectAnimalsFromEnclosure();

                if (carriedAnimals.isEmpty()) {
                    System.out.println("[Tick] Farmer " + id + " is waiting for new deliveries...");
                    Thread.sleep(100);
                    continue;
                }

                moveAnimalsToFields(carriedAnimals);

                System.out.println("[Tick] Farmer " + id + " returns to the enclosure.");
                Thread.sleep(10 * 100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<AnimalType> collectAnimalsFromEnclosure() throws InterruptedException {
        List<AnimalType> carriedAnimals = new ArrayList<>();
            while (carriedAnimals.size() < max_animals_carry) {
            AnimalType animal = farm.getEnclosure().takeAnimal();
            carriedAnimals.add(animal);
        }
    
        System.out.println("[Tick] Farmer " + id + " collected " + carriedAnimals.size() + " animals.");
        return carriedAnimals;
    }
    

    private void moveAnimalsToFields(List<AnimalType> carriedAnimals) throws InterruptedException {
        Map<AnimalType, Integer> groupedAnimals = new HashMap<>();

        for (AnimalType animal : carriedAnimals) {
            groupedAnimals.put(animal, groupedAnimals.getOrDefault(animal, 0) + 1);
        }

        for (Map.Entry<AnimalType, Integer> entry : groupedAnimals.entrySet()) {
            AnimalType type = entry.getKey();
            int count = entry.getValue();
            Field field = farm.getField(type);

            int travelTime = 10 + count;
            System.out.println("[Tick] Farmer " + id + " walks to " + type + " field (" + travelTime + " ticks)");
            Thread.sleep(travelTime * 100);

            field.getLock().lock();
            int stocked = 0;
            try {
                for (int i = 0; i < count; i++) {
                    if (field.addAnimal()) {
                        stocked++;
                    } else {
                        break; // Field is full
                    }
                }
                System.out.println("[Tick] Farmer " + id + " stocked " + stocked + " " + type);
            } finally {
                field.getLock().unlock();
            }

            Thread.sleep(stocked * 100);
        }
    }
}
