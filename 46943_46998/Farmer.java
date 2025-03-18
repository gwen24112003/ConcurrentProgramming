import java.util.Random;
import java.util.*;

class Farmer extends Thread {
    private final int id;
    private final Farm farm;
    private final TickManager tickManager;
    private static final int max_animals_carry = 10;

    public Farmer(int id, Farm farm, TickManager tickManager) {
        this.id = id;
        this.farm = farm;
        this.tickManager = tickManager;
    }

    @Override
    public void run() {
        try {
            while (true) {
                List<AnimalType> carriedAnimals = collectAnimalsFromEnclosure();
                if (carriedAnimals.isEmpty()) {
                    System.out.println("Farmer " + id + " is waiting for deliveries...");
                    Thread.sleep(500);
                    continue;
                }
                moveAnimalsToFields(carriedAnimals);
                System.out.println("Farmer " + id + " returns to the enclosure.");
                Thread.sleep(10 * 100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private List<AnimalType> collectAnimalsFromEnclosure() throws InterruptedException {
        List<AnimalType> carriedAnimals = new ArrayList<>();
        while (carriedAnimals.size() < max_animals_carry) {
            AnimalType animal = farm.getEnclosure().takeAnimal();
            if (animal == null) {
                Thread.sleep(500);
                continue;
            }
            carriedAnimals.add(animal);
        }
        System.out.println("Farmer " + id + " collected " + carriedAnimals.size() + " animals.");
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
            System.out.println("Farmer " + id + " walks to " + type + " field (" + travelTime + " ticks)");
            Thread.sleep(travelTime * 100);
            field.getLock().lock();
            try {
                for (int i = 0; i < count; i++) {
                    if (field.addAnimal()) {
                        System.out.println("✅ Farmer " + id + " stocked " + type);
                    }
                }
            } finally {
                field.getLock().unlock();
            }
            Thread.sleep(count * 100);
        }
    }
}
