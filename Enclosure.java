import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Enclosure {
    private final BlockingQueue<AnimalType> animals = new LinkedBlockingQueue<>();
    private final Random random = new Random();

    public synchronized void addAnimals(AnimalType[] newAnimals) {
        for (AnimalType animal : newAnimals) {
            animals.offer(animal);
        }
        logDelivery(newAnimals);
    }

    public synchronized AnimalType[] takeAnimals(int maxCount) throws InterruptedException {
        AnimalType[] takenAnimals = new AnimalType[Math.min(maxCount, animals.size())];
        for (int i = 0; i < takenAnimals.length; i++) {
            takenAnimals[i] = animals.take();
        }
        return takenAnimals;
    }

    public synchronized AnimalType takeAnimal() throws InterruptedException {
        return animals.poll(); // Renvoie null si vide
    }

    public boolean isEmpty() {
        return animals.isEmpty();
    }

    public int getAnimalCount() {
        return animals.size();
    }

    public void simulateDeliveries(int tickCount) {
        if (random.nextDouble() < 0.01) {
            AnimalType[] delivery = generateRandomDelivery();
            addAnimals(delivery);
            System.out.println(tickCount + " Delivery arrived: " + formatAnimalCounts(delivery));
        }
    }

    private AnimalType[] generateRandomDelivery() {
        AnimalType[] delivery = new AnimalType[10];
        for (int i = 0; i < 10; i++) {
            delivery[i] = AnimalType.values()[random.nextInt(AnimalType.values().length)];
        }
        return delivery;
    }

    private void logDelivery(AnimalType[] animals) {
        System.out.println("📦 Enclosure received: " + formatAnimalCounts(animals));
    }

    private String formatAnimalCounts(AnimalType[] animals) {
        Map<AnimalType, Integer> countMap = new HashMap<>();
        for (AnimalType animal : animals) {
            countMap.put(animal, countMap.getOrDefault(animal, 0) + 1);
        }
        return countMap.toString();
    }
}
