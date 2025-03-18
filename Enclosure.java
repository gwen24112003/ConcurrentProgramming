import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Enclosure {
    private final BlockingQueue<AnimalType> animals = new LinkedBlockingQueue<>(); // Thread-safe queue

    /**
     * Adds multiple animals to the enclosure (simulating a delivery).
     *
     * @param newAnimals Array of animals being delivered.
     */
    public void addAnimals(AnimalType[] newAnimals) {
        for (AnimalType animal : newAnimals) {
            animals.offer(animal); // (non-blocking add)
            System.out.println("Added to enclosure: " + animal);
        }
    }

    /**
     * Retrieves an animal from the enclosure.
     * If empty, it waits for an animal to become available.
     *
     * @return The next available animal.
     * @throws InterruptedException If thread is interrupted while waiting.
     */
    public AnimalType takeAnimal() throws InterruptedException {
        return animals.take();
    }

    /**
     * Checks if the enclosure is empty.
     *
     * @return True if there are no animals in storage.
     */
    public boolean isEmpty() {
        return animals.isEmpty();
    }

    /**
     * Gets the number of animals currently in the enclosure.
     *
     * @return The number of stored animals.
     */
    public int getAnimalCount() {
        return animals.size();
    }
}
